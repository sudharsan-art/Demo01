package com.app.employeedesk.repo;

import com.app.employeedesk.entity.OdRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface OdRequestRepository  extends JpaRepository<OdRequest,String> {
    Optional<OdRequest> findByEmployeeIdAndOdDateAndStatus(
            String employeeId, LocalDate odDate, String status);

    List<OdRequest> findByEmployeeId(String employeeId);
}

