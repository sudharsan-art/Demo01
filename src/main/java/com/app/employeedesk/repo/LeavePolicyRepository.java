package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, UUID> {
    List<LeavePolicy> findByEmployeeIdAndActiveTrue(UUID employeeId);

    Optional<LeavePolicy> findByEmployeeIdAndLeaveCode(UUID employeeId, String leaveCode);

    // role templates (employee is null)
    List<LeavePolicy> findByRoleAndEmployeeIsNullAndActiveTrue(String role);
}