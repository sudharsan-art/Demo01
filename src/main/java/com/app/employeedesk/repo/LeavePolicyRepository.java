package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LeaveMaster;
import com.app.employeedesk.entity.LeavePolicy;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, UUID> {

    // employee policies
    List<LeavePolicy> findByEmployee_IdAndActiveTrue(UUID employeeId);

    // find policy using employee + leaveCode
    Optional<LeavePolicy> findByEmployee_IdAndLeave_LeaveCode(
            UUID employeeId,
            String leaveCode
    );

    // role templates (employee = null)
    List<LeavePolicy> findByRoleAndEmployeeIsNullAndActiveTrue(String role);

    // direct relation lookup
    Optional<LeavePolicy> findByEmployeeAndLeave(
            UserDetails employee,
            LeaveMaster leave
    );
}