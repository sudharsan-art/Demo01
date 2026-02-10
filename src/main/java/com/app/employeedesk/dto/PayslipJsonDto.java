package com.app.employeedesk.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayslipJsonDto {
    private String name;
    private String employeeCode;
    private String designation;
    private String department;
    private String basicSalary;
    private String netSalary;
    private String allowanceList;
    private String  deductionList;
}
