package com.app.employeedesk.service;

import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class AttendanceV2Service {

    private final AttendanceV2Repository attendanceRepository;

    private final LateTrackerV2Repository lateTrackerRepository;

    private final AttendancePolicyV2Repository policyRepository;


    private final UserDetailsRepository userDetailsRepository;


    private final LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveRequestRepositoryV2 leaveRequestRepositoryV2;

    @Autowired
    private LeaveMasterRepository leaveMasterRepository;


    public String punchIn(String empId) {

        UUID employeeId;
        try {
            employeeId = UUID.fromString(empId);
        } catch (Exception e) {
            throw new RuntimeException("Invalid employee ID format");
        }

        UserDetails employee = userDetailsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("Employee is not active");
        }
        LocalDate today = LocalDate.now();

        if (attendanceRepository.existsByEmployeeAndAttendanceDate(employee, today)) {
            throw new RuntimeException("Already punched in today");
        }

        AttendancePolicyV2 policy = policyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        LocalDateTime now = LocalDateTime.now();

        AttendanceV2 attendance = new AttendanceV2();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setPunchInTime(now);

        // Late logic
        LocalTime allowedTime = policy.getOfficeStartTime()
                .plusMinutes(policy.getLateGraceMinutes());

        int lateCount = 0;

        if (now.toLocalTime().isAfter(allowedTime)) {
            lateCount = 1;
        }

        attendance.setLateCount(lateCount);

        attendanceRepository.save(attendance);

        return "Punch In Successful";
    }

    public String punchOut(String empId) {

        UUID employeeId;
        try {
            employeeId = UUID.fromString(empId);
        } catch (Exception e) {
            throw new RuntimeException("Invalid employee ID format");
        }

        UserDetails employee = userDetailsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        AttendanceV2 attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, today)
                .orElseThrow(() -> new RuntimeException("Punch in not found"));

        if (attendance.getPunchOutTime() != null) {
            throw new RuntimeException("Already punched out");
        }

        AttendancePolicyV2 policy = policyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Policy not found"));


        LocalDateTime now = LocalDateTime.now();
        attendance.setPunchOutTime(now);

        // Work hours
        double hours = Duration.between(
                attendance.getPunchInTime(),
                now
        ).toMinutes() / 60.0;

        attendance.setWorkHours(hours);

        // Early leaving
        if (now.toLocalTime().isBefore(policy.getOfficeEndTime())) {
            attendance.setLateCount(attendance.getLateCount() + 1);
        }

        // Status
        if (hours >= 8) {
            attendance.setStatus("PRESENT");
        } else if (hours >= 4) {
            attendance.setStatus("HALF_DAY");
        } else {
            attendance.setStatus("ABSENT");
        }

        attendanceRepository.save(attendance);

        updateLateTracker(employee, attendance.getLateCount());

        return "Punch Out Successful";
    }

    private void updateLateTracker(UserDetails emp, int todayLate) {

        if (todayLate == 0) return;

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        LateTrackerV2 tracker = lateTrackerRepository
                .findByEmployeeAndMonthAndYear(emp, month, year)
                .orElseGet(() -> {
                    LateTrackerV2 lt = new LateTrackerV2();
                    lt.setId(UUID.randomUUID().toString());
                    lt.setEmployee(emp);
                    lt.setMonth(month);
                    lt.setYear(year);
                    lt.setTotalLateCount(0);
                    lt.setDeductedLeaveCount(0);
                    return lt;
                });

        tracker.setTotalLateCount(tracker.getTotalLateCount() + todayLate);

        lateTrackerRepository.save(tracker);
        handleLateDeduction(emp, tracker);
    }

    private void handleLateDeduction(UserDetails employee, LateTrackerV2 tracker) {

        AttendancePolicyV2 policy = policyRepository.findAll().get(0);

        int maxLate = policy.getMaxLateAllowed();

        int totalLate = tracker.getTotalLateCount();

        int shouldDeduct = totalLate / maxLate;

        int alreadyDeducted = tracker.getDeductedLeaveCount();

        int newDeduction = shouldDeduct - alreadyDeducted;

        if (newDeduction <= 0) return;


        for (int i = 0; i < newDeduction; i++) {

            //  1. Get Leave Balance (example: Casual Leave)

            LeaveMaster leave = getDefaultLeave();

            LeaveBalance leaveBalance = leaveBalanceRepository
                    .findByEmployeeAndLeave(employee, leave)
                    .orElseThrow(() -> new RuntimeException("Leave balance not found"));


            //  2. Deduct leave
            if (leaveBalance.getRemainingDays() <= 0) {

                throw new RuntimeException("No leave balance (LOP case handle later)");
            }

            leaveBalance.setUsedDays(leaveBalance.getUsedDays() + 1);
            leaveBalance.setRemainingDays(leaveBalance.getRemainingDays() - 1);

            leaveBalanceRepository.save(leaveBalance);

            //  3. Create Leave Request
            LeaveRequestV2 request = new LeaveRequestV2();
            request.setEmployee(employee);
            request.setLeave(leave);
            request.setFromDate(LocalDate.now());
            request.setToDate(LocalDate.now());
            request.setNumberOfDays(1);
            request.setReason("Auto deducted due to late policy");
            request.setStatus(LeaveStatus.ACCEPT);

            leaveRequestRepositoryV2.save(request);
        }

        tracker.setDeductedLeaveCount(alreadyDeducted + newDeduction);
        lateTrackerRepository.save(tracker);
    }


    private LeaveMaster getDefaultLeave() {
        return leaveMasterRepository.findByLeaveCode("CL")
                .orElseThrow(() -> new RuntimeException("CL leave not found"));
    }
}
