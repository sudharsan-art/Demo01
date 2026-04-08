package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AttendanceDTOv2;
import com.app.employeedesk.dto.AttendanceMonthlyReportDTO;
import com.app.employeedesk.dto.LateSummaryDTO;
import com.app.employeedesk.entity.AttendanceV2;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.mapper.AttendanceMapper;
import com.app.employeedesk.repo.AttendanceV2Repository;
import com.app.employeedesk.repo.LateTrackerV2Repository;
import com.app.employeedesk.repo.UserDetailsRepository;
import com.app.employeedesk.service.AttendanceV2Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/attendance/v2")
public class AttendanceV2Controller {

    @Autowired
    private AttendanceV2Service service;

    @Autowired
    private AttendanceV2Repository attendanceRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private LateTrackerV2Repository lateTrackerRepository;

    @Autowired
    private AttendanceMapper attendanceMapper;


    @PostMapping("/punch-in/{empId}")
    public String punchIn(@PathVariable String empId) {
        return service.punchIn(empId);
    }

    @PostMapping("/punch-out/{empId}")
    public String punchOut(@PathVariable String empId) {
        return service.punchOut(empId);
    }


    @GetMapping("/today/{empId}")
    public AttendanceDTOv2 getTodayAttendance(@PathVariable String empId) {

        UUID employeeId = UUID.fromString(empId);

        UserDetails employee = userDetailsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        AttendanceV2 attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No attendance found"));

        return AttendanceDTOv2.builder()
                .employeeId(attendance.getEmployee().getId().toString())
                .employeeName(attendance.getEmployee().getName())
                .punchInTime(attendance.getPunchInTime())
                .punchOutTime(attendance.getPunchOutTime())
                .lateCount(attendance.getLateCount())
                .workHours(attendance.getWorkHours())
                .status(attendance.getStatus())
                .build();
    }


    @GetMapping("/monthly/{empId}")
    public AttendanceMonthlyReportDTO getMonthlyReport(@PathVariable String empId) {

        UUID employeeId = UUID.fromString(empId);


        UserDetails employee = userDetailsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();



        List<AttendanceV2> records = attendanceRepository.findByEmployee(employee);

        List<AttendanceV2> filtered = records.stream()
                .filter(a -> a.getAttendanceDate().getMonthValue() == month)
                .filter(a -> a.getAttendanceDate().getYear() == year)
                .toList();

        long present = filtered.stream()
                .filter(a -> "PRESENT".equals(a.getStatus()))
                .count();

        long halfDay = filtered.stream()
                .filter(a -> "HALF_DAY".equals(a.getStatus()))
                .count();

        long absent = filtered.stream()
                .filter(a -> "ABSENT".equals(a.getStatus()))
                .count();

        double totalHours = filtered.stream()
                .mapToDouble(a -> a.getWorkHours() == null ? 0 : a.getWorkHours())
                .sum();


        return AttendanceMonthlyReportDTO.builder()
                .presentDays(present)
                .halfDays(halfDay)
                .absentDays(absent)
                .totalWorkHours(totalHours)
                .build();
    }


    @GetMapping("/late-summary/{empId}")
    public LateSummaryDTO getLateSummary(@PathVariable String empId) {

        UUID employeeId = UUID.fromString(empId);

        UserDetails employee = userDetailsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        var tracker = lateTrackerRepository
                .findByEmployeeAndMonthAndYear(employee, month, year)
                .orElseThrow(() -> new RuntimeException("No late data"));


        return LateSummaryDTO.builder()
                .totalLateCount(tracker.getTotalLateCount())
                .deductedLeaves(tracker.getDeductedLeaveCount())
                .build();
    }


    @GetMapping("/admin/all")
    public List<AttendanceDTOv2> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(AttendanceMapper::toTodayDTO)
                .toList();
    }
}