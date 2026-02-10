package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.entity.EmployeePaySlipAllowances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface EmployeePaySlipAllowancesRepository extends JpaRepository<EmployeePaySlipAllowances, UUID> {
    @Query("SELECT new com.app.employeedesk.dto.EmployeePaySlipAllowanceDto(a.id,a.percentage,a.reason) " +
            "from EmployeePaySlipAllowances a WHERE a.employeePayRollAndPaySlips.id =:payslipId")
    List<EmployeePaySlipAllowanceDto> getEmployeePaySlipAllowanceDetails(UUID payslipId);
}
