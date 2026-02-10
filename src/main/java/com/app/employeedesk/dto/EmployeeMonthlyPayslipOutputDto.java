package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.DepartmentType;
import com.app.employeedesk.enumeration.DesignationType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMonthlyPayslipOutputDto {
    private UUID employeeId;
    private String empName;
    private String employeeCode;
    private DepartmentType department;
    private DesignationType designation;
    private String payslipData;
    private LocalDate generatedOn;

}

