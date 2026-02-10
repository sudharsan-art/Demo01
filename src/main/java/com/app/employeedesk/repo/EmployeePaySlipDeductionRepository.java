package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.dto.EmployeePaySlipDeductionDto;
import com.app.employeedesk.entity.EmployeePaySlipDeductions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeePaySlipDeductionRepository extends JpaRepository<EmployeePaySlipDeductions, UUID> {
    @Query("SELECT new com.app.employeedesk.dto.EmployeePaySlipDeductionDto(d.id,d.percentage,d.reason) " +
            "from EmployeePaySlipDeductions d WHERE d.employeePayRollAndPaySlips.id =:payslipId")
    List<EmployeePaySlipDeductionDto> getEmployeePaySlipDeductionDetails(UUID payslipId);
}
