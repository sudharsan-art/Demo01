package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeShiftAssignmentV3;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeShiftAssignmentV3Repository extends JpaRepository<EmployeeShiftAssignmentV3, UUID> {
    Optional<EmployeeShiftAssignmentV3> findTopByEmployeeAndFromDateLessThanEqualOrderByFromDateDesc(
            UserDetails employee, LocalDate date);

    boolean existsByEmployeeAndFromDate(UserDetails employee, LocalDate fromDate);
}
