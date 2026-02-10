package com.app.employeedesk.repo;


import com.app.employeedesk.dto.EmployeePayRollPaySlipDto;
import com.app.employeedesk.entity.EmployeePayRollAndPaySlips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface EmployeePayRollPaySlipRepository extends JpaRepository<EmployeePayRollAndPaySlips , UUID> {
    @Query("SELECT new com.app.employeedesk.dto.EmployeePayRollPaySlipDto(e.employeeBasicDetails.id,e.id,e.basicSalary) " +
            "from EmployeePayRollAndPaySlips e WHERE e.employeeBasicDetails.id =:employeeId")
    Optional<EmployeePayRollPaySlipDto> getEmployeePaySlipDetails(UUID employeeId);
}
