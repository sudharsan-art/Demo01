package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LeaveBalance;
import com.app.employeedesk.entity.LeaveMaster;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, UUID> {

    Optional<LeaveBalance> findByEmployeeAndLeave(
            UserDetails employee,
            LeaveMaster leave
    );


}
