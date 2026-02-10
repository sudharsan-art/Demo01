package com.app.employeedesk.dto;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePayRollPaySlipDto {
    private UUID employeeId;
    private UUID payslipId;
    private Double basicSalary;
    private List<EmployeePaySlipAllowanceDto> employeePaySlipAllowanceDto;
    private List<EmployeePaySlipDeductionDto> employeePaySlipDeductionDto;

    public EmployeePayRollPaySlipDto(UUID employeeId, UUID payslipId, Double basicSalary) {
        this.employeeId = employeeId;
        this.payslipId = payslipId;
        this.basicSalary = basicSalary;
    }
}
