package com.app.employeedesk.repo;

import com.app.employeedesk.entity.ComboOffLeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Repository
public interface ComboOffRepository extends JpaRepository<ComboOffLeaveRequest, UUID> {

}
