package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LeaveMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
public interface LeaveMasterRepository extends JpaRepository<LeaveMaster, UUID> {
    Optional<LeaveMaster> findByLeaveCode(String leaveCode);

}
