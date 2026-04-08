package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeShiftV2;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeShiftV2Repository extends JpaRepository<EmployeeShiftV2, String> {
    Optional<EmployeeShiftV2> findTopByEmployeeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
            UserDetails employee, LocalDate date
    );
}
