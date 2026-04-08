package com.app.employeedesk.service;

import com.app.employeedesk.dto.LeaveRequestV2Dto;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveServiceV2 {
    private final LeaveRequestRepositoryV2 leaveRequestRepositoryV2;
    private final UserDetailsRepository userDetailsRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveMasterRepository leaveMasterRepository;
    private final YearMasterRepository yearMasterRepository;

    @Transactional
    public void applyLeave(LeaveRequestV2Dto dto, String username) {

        LeaveMaster leaveMaster = leaveMasterRepository
                .findByLeaveCode(dto.getLeaveCode())
                .orElseThrow(() -> new RuntimeException("Invalid leave type"));


        //  Get employee
        UserDetails employee = userDetailsRepository
                .findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Validate date range
        if (dto.getFromDate() == null || dto.getToDate() == null) {
            throw new RuntimeException("From date and To date are required");
        }

        if (dto.getFromDate().isAfter(dto.getToDate())) {
            throw new RuntimeException("Invalid date range");
        }
        if (dto.getFromDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot apply leave for past dates");
        }

        //  Calculate number of days

        boolean isHalfDay = Boolean.TRUE.equals(dto.getHalfDay());
        if (isHalfDay && !dto.getFromDate().equals(dto.getToDate())) {
            throw new RuntimeException("Half-day leave can only be applied for a single day");
        }
        if (isHalfDay && dto.getHalfDaySession() == null) {
            throw new RuntimeException("Half-day session is required for half-day leave");
        }
        if (!isHalfDay && dto.getHalfDaySession() != null) {
            throw new RuntimeException("Half-day session should only be provided for half-day leave");
        }

        double days = calculateDays(dto.getFromDate(), dto.getToDate(), isHalfDay);


        if (days <= 0) {
            throw new RuntimeException("Invalid leave duration");
        }

        // Check if LeaveBalance exists for this leave type

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeave(
                        employee,
                        leaveMaster)
                .orElseThrow(() ->
                        new RuntimeException("Leave balance not initialized"));

        //  Optional: Prevent overlapping leave requests
        boolean overlapExists = leaveRequestRepositoryV2
                .existsByEmployee_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        employee.getId(),
                        dto.getToDate(),
                        dto.getFromDate());

        if (overlapExists) {
            throw new RuntimeException("Leave dates overlap with existing request");
        }

        YearMaster year = yearMasterRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        dto.getFromDate(),
                        dto.getFromDate()
                )
                .orElseThrow(() -> new RuntimeException("Financial year not configured"));

        //  Create LeaveRequest
        LeaveRequestV2 leave = LeaveRequestV2.builder()
                .employee(employee)
                .leave(leaveMaster)
                .year(year)
                .fromDate(dto.getFromDate())
                .toDate(dto.getToDate())
                .numberOfDays(days)
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        leaveRequestRepositoryV2.save(leave);
    }


    @Transactional
    public void approveLeave(UUID leaveId, LeaveStatus status) {
        LeaveRequestV2 leave = leaveRequestRepositoryV2.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Leave already processed");
        }

        if (status == LeaveStatus.ACCEPT) {

            validateLeaveLimits(leave);

            LeaveMaster leaveMaster = leave.getLeave();

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeAndLeave(
                            leave.getEmployee(),
                            leaveMaster)
                    .orElseThrow(() -> new RuntimeException("Balance not found"));

            if (balance.getRemainingDays() < leave.getNumberOfDays()) {
                throw new RuntimeException("Insufficient leave balance");
            }

            balance.setUsedDays(balance.getUsedDays() + leave.getNumberOfDays());
            balance.setRemainingDays(balance.getRemainingDays() - leave.getNumberOfDays());

            leaveBalanceRepository.save(balance);
        }

        leave.setStatus(status);
        leaveRequestRepositoryV2.save(leave);

    }


    private double calculateDays(LocalDate from, LocalDate to, boolean halfDay) {

        if (halfDay) {
            return 0.5;
        }

        List<LocalDate> days = getLeaveDays(from, to);

        return days.size();
    }


    private int getFinancialQuarter(LocalDate date) {

        int month = date.getMonthValue();

        if (month >= 4 && month <= 6) return 1;
        if (month >= 7 && month <= 9) return 2;
        if (month >= 10 && month <= 12) return 3;

        return 4; // Jan–Mar
    }


    private int getQuarter(LocalDate date) {

        int month = date.getMonthValue();

        if (month <= 3) return 1;
        if (month <= 6) return 2;
        if (month <= 9) return 3;

        return 4;
    }


    //Weekends should not consume leave cap
    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 6;
    }


    //remove weekends
//
//split across quarters
//
//count working days only
    private List<LocalDate> getLeaveDays(LocalDate start, LocalDate end) {
        List<LocalDate> days = new ArrayList<>();

        LocalDate current = start;

        while (!current.isAfter(end)) {
            if (!isWeekend(current)) {
                days.add(current);
            }
            current = current.plusDays(1);
        }

        return days;
    }


    private void validateQuarterLimit(LeaveRequestV2 leave) {

        UUID employeeId = leave.getEmployee().getId();

        List<LeaveRequestV2> approvedLeaves =
                leaveRequestRepositoryV2.findByEmployee_IdAndStatus(
                        employeeId,
                        LeaveStatus.ACCEPT);

        Map<String, Double> quarterUsage = new HashMap<>();

        for (LeaveRequestV2 approved : approvedLeaves) {

            List<LocalDate> days =
                    getLeaveDays(approved.getFromDate(), approved.getToDate());

            for (LocalDate d : days) {

                int q = getQuarter(d);
                int year = d.getYear();

                String key = year + "-Q" + q;

                quarterUsage.put(key,
                        quarterUsage.getOrDefault(key, 0.0) + 1);
            }
        }

        List<LocalDate> newDays =
                getLeaveDays(leave.getFromDate(), leave.getToDate());

        for (LocalDate d : newDays) {

            int q = getQuarter(d);
            int year = d.getYear();

            String key = year + "-Q" + q;

            double used = quarterUsage.getOrDefault(key, 0.0);

            if (used + 1 > 5) {
                throw new RuntimeException(
                        "Quarter leave limit exceeded (5 days)");
            }

            quarterUsage.put(key, used + 1);
        }
    }

    private void validateLeaveLimits(LeaveRequestV2 leave) {

        UUID yearId = leave.getYear().getId();

        List<LeaveRequestV2> approvedLeaves =
                leaveRequestRepositoryV2
                        .findByEmployee_IdAndYear_IdAndStatus(
                                leave.getEmployee().getId(),
                                yearId,
                                LeaveStatus.ACCEPT
                        );

        Map<String, Integer> monthUsage = new HashMap<>();
        Map<String, Integer> quarterUsage = new HashMap<>();

        for (LeaveRequestV2 approved : approvedLeaves) {

            LocalDate current = approved.getFromDate();

            while (!current.isAfter(approved.getToDate())) {

                if (!isWeekend(current)) {

                    int month = current.getMonthValue();
                    int year = current.getYear();

                    int quarter = getFinancialQuarter(current);

                    String monthKey = year + "-" + month;
                    String quarterKey = year + "-Q" + quarter;

                    monthUsage.put(
                            monthKey,
                            monthUsage.getOrDefault(monthKey, 0) + 1
                    );

                    quarterUsage.put(
                            quarterKey,
                            quarterUsage.getOrDefault(quarterKey, 0) + 1
                    );
                }

                current = current.plusDays(1);
            }
        }

        LocalDate current = leave.getFromDate();

        while (!current.isAfter(leave.getToDate())) {

            if (!isWeekend(current)) {

                int month = current.getMonthValue();
                int year = current.getYear();

                int quarter = getFinancialQuarter(current);

                String monthKey = year + "-" + month;
                String quarterKey = year + "-Q" + quarter;

                int monthUsed = monthUsage.getOrDefault(monthKey, 0);
                int quarterUsed = quarterUsage.getOrDefault(quarterKey, 0);

                if (monthUsed + 1 > 3) {
                    throw new RuntimeException(
                            "Monthly leave limit exceeded (max 3 days)"
                    );
                }

                if (quarterUsed + 1 > 5) {
                    throw new RuntimeException(
                            "Quarter leave limit exceeded (max 5 days)"
                    );
                }

                monthUsage.put(monthKey, monthUsed + 1);
                quarterUsage.put(quarterKey, quarterUsed + 1);
            }

            current = current.plusDays(1);
        }
    }

    @Transactional
    public void cancelLeave(UUID leaveId) {

        LeaveRequestV2 leave = leaveRequestRepositoryV2.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getStatus() == LeaveStatus.REJECT) {
            throw new RuntimeException("Rejected leave cannot be cancelled");
        }

        if (leave.getStatus() == LeaveStatus.ACCEPT &&
                leave.getFromDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel leave after it has started");
        }

        leave.setStatus(LeaveStatus.CANCELLED);

        leaveRequestRepositoryV2.save(leave);
    }
}
