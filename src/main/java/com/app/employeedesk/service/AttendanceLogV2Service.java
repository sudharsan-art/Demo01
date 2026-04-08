package com.app.employeedesk.service;

import com.app.employeedesk.controlleradvice.ObjectInvalidException;
import com.app.employeedesk.dto.AttendancePunchDTO;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.PunchType;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AttendanceLogV2Service {
    @Autowired
    private AttendanceLogV2Repository logRepository;

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private AttendanceV2Repository attendanceRepository;

    @Autowired
    private EmployeeShiftAssignmentV3Repository shiftAssignmentRepository;

    @Autowired
    private HolidayV2Repository holidayRepository;

    public String punch(AttendancePunchDTO dto) {
        UUID empId = validateUUID(dto.getEmployeeId());
        UserDetails employee = userRepository.findById(empId)
                .orElseThrow(() -> new ObjectInvalidException("Employee not found"));

        if (employee.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("Employee is inactive");
        }

        LocalDate today = LocalDate.now();

        if (holidayRepository.existsByDate(today)) {
            throw new RuntimeException("Today is holiday");
        }

        DayOfWeek day = today.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Weekly off");
        }

        List<AttendanceLogV2> logs =
                logRepository.findByEmployeeAndAttendanceDateOrderByPunchTime(employee, today);

        validatePunchSequence(logs, dto.getType());

        AttendanceLogV2 log = AttendanceLogV2.builder()
                .id(UUID.randomUUID().toString())
                .employee(employee)
                .attendanceDate(today)
                .punchTime(LocalDateTime.now())
                .type(dto.getType())
                .build();

        logRepository.save(log);
        updateAttendanceSummary(employee, today, logs, dto.getType());

        return "Punch " + dto.getType() + " recorded";
    }

    private void updateAttendanceSummary(UserDetails employee, LocalDate date, List<AttendanceLogV2> logs, PunchType newType) {
        ShiftMasterV3 shift = getShiftForEmployee(employee, date);
        
        AttendanceV2 attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, date)
                .orElseGet(() -> {
                    AttendanceV2 a = new AttendanceV2();
                    a.setId(UUID.randomUUID().toString());
                    a.setEmployee(employee);
                    a.setAttendanceDate(date);
                    return a;
                });

        // 1. RAW HOURS: Time between first IN and last OUT (True clock time)
        LocalDateTime firstInTime = logs.get(0).getPunchTime();
        LocalDateTime lastOutTime = LocalDateTime.now();
        double actualHours = Duration.between(firstInTime, lastOutTime).toMinutes() / 60.0;
        attendance.setActualHours(actualHours);

        // 2. SMARTER WORK HOURS (Clipping logic)
        double workHours = calculateWorkHoursWithShiftWindow(logs, shift);
        attendance.setWorkHours(workHours);

        // 3. LATE & EARLY LEAVE LOGIC
        LocalTime firstIn = firstInTime.toLocalTime();
        int lateCount = 0;

        // Late Check (using Shift's dynamic Grace In Time)
        if (firstIn.isAfter(shift.getStartTime().plusMinutes(shift.getGraceInTime()))) {
            lateCount++;
        }

        // Early Leave Check (using Shift's End Time)
        if (newType == PunchType.OUT) {
            LocalTime lastOut = lastOutTime.toLocalTime();
            if (lastOut.isBefore(shift.getEndTime())) {
                attendance.setEarlyLeaving(true);
                lateCount++;
            } else {
                attendance.setEarlyLeaving(false);
            }
        }

        // Buffer Warning (Punch in > 30 mins before shift start)
        if (firstIn.isBefore(shift.getStartTime().minusMinutes(30))) {
            attendance.setBufferWarning(true);
        } else {
            attendance.setBufferWarning(false);
        }

        attendance.setLateCount(lateCount);

        // 4. STATUS LOGIC (Using Shift's Min Hours)
        if (workHours >= shift.getMinHoursFullDay()) {
            attendance.setStatus("PRESENT");
        } else if (workHours >= shift.getMinHoursHalfDay()) {
            attendance.setStatus("HALF_DAY");
        } else {
            attendance.setStatus("ABSENT");
        }

        attendanceRepository.save(attendance);
    }

    private double calculateWorkHoursWithShiftWindow(List<AttendanceLogV2> logs, ShiftMasterV3 shift) {
        double totalMinutes = 0;
        LocalTime shiftStart = shift.getStartTime();

        for (int i = 0; i < logs.size() - 1; i += 2) {
            LocalTime inTime = logs.get(i).getPunchTime().toLocalTime();
            LocalTime outTime = logs.get(i + 1).getPunchTime().toLocalTime();

            // CLIPPING LOGIC: If punched in at 7 AM for a 9 AM shift, start count from 9 AM
            if (i == 0 && inTime.isBefore(shiftStart)) {
                inTime = shiftStart;
            }

            if (outTime.isAfter(inTime)) {
                totalMinutes += Duration.between(inTime, outTime).toMinutes();
            }
        }
        
        // Handle the current open punch if it's an OUT punch
        if (logs.size() % 2 == 0) {
            LocalTime lastIn = logs.get(logs.size() - 2).getPunchTime().toLocalTime();
            LocalTime lastOut = logs.get(logs.size() - 1).getPunchTime().toLocalTime();
            
            if (logs.size() == 2 && lastIn.isBefore(shiftStart)) {
                lastIn = shiftStart;
            }
            
            if (lastOut.isAfter(lastIn)) {
                totalMinutes += Duration.between(lastIn, lastOut).toMinutes();
            }
        }

        return totalMinutes / 60.0;
    }

    private ShiftMasterV3 getShiftForEmployee(UserDetails employee, LocalDate date) {
        return shiftAssignmentRepository
                .findTopByEmployeeAndFromDateLessThanEqualOrderByFromDateDesc(employee, date)
                .map(EmployeeShiftAssignmentV3::getShift)
                .orElseThrow(() -> new RuntimeException("No shift assigned for this date in V3"));
    }

    private void validatePunchSequence(List<AttendanceLogV2> logs, PunchType type) {
        if (logs.isEmpty()) {
            if (type == PunchType.OUT) throw new RuntimeException("First punch must be IN");
            return;
        }
        PunchType lastType = logs.get(logs.size() - 1).getType();
        if (lastType == type) throw new RuntimeException("Sequence error: cannot have " + type + " twice");
    }

    private UUID validateUUID(String id) {
        try { return UUID.fromString(id); } catch (Exception e) { throw new RuntimeException("Invalid UUID"); }
    }
}
