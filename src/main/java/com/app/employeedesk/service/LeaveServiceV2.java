package com.app.employeedesk.service;

import com.app.employeedesk.dto.LeaveRequestV2Dto;
import com.app.employeedesk.entity.LeaveBalance;
import com.app.employeedesk.entity.LeavePolicy;
import com.app.employeedesk.entity.LeaveRequestV2;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.repo.LeaveBalanceRepository;
import com.app.employeedesk.repo.LeavePolicyRepository;
import com.app.employeedesk.repo.LeaveRequestRepositoryV2;
import com.app.employeedesk.repo.UserDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void applyLeave(LeaveRequestV2Dto dto, String username) {

        // 1️⃣ Get employee
        UserDetails employee = userDetailsRepository
                .findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Validate date range
        if (dto.getFromDate() == null || dto.getToDate() == null) {
            throw new RuntimeException("From date and To date are required");
        }

        if (dto.getFromDate().isAfter(dto.getToDate())) {
            throw new RuntimeException("Invalid date range");
        }

        // 3️⃣ Calculate number of days
        int days = calculateDays(dto.getFromDate(), dto.getToDate());

        if (days <= 0) {
            throw new RuntimeException("Invalid leave duration");
        }

        // 4️⃣ Check if LeaveBalance exists for this leave type
        String leaveCode = dto.getLeaveType().name();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveCode(
                        employee.getId(),
                        leaveCode)
                .orElseThrow(() ->
                        new RuntimeException("Leave balance not initialized"));

        // 5️⃣ Optional: Prevent overlapping leave requests
        boolean overlapExists = leaveRequestRepositoryV2
                .existsByEmployee_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        employee.getId(),
                        dto.getToDate(),
                        dto.getFromDate());

        if (overlapExists) {
            throw new RuntimeException("Leave dates overlap with existing request");
        }

        // 6️⃣ Create LeaveRequest
        LeaveRequestV2 leave = LeaveRequestV2.builder()
                .employee(employee)
                .leaveType(dto.getLeaveType())
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

            String leaveCode = leave.getLeaveType().name();

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeIdAndLeaveCode(
                            leave.getEmployee().getId(),
                            leaveCode)
                    .orElseThrow(() -> new RuntimeException("Balance not found"));

            if (balance.getRemainingDays() < leave.getNumberOfDays()) {
                throw new RuntimeException("Insufficient leave balance");
            }
            balance.setUsedDays(
                    balance.getUsedDays() + leave.getNumberOfDays());

            balance.setRemainingDays(
                    balance.getRemainingDays() - leave.getNumberOfDays());

            leaveBalanceRepository.save(balance);
        }
        leave.setStatus(status);
        leaveRequestRepositoryV2.save(leave);

    }


    private int calculateDays(LocalDate from, LocalDate to) {
        return (int) ChronoUnit.DAYS.between(from, to) + 1;
    }
}
