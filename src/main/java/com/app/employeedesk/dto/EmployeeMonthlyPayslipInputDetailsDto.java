package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class EmployeeMonthlyPayslipInputDetailsDto {
    private UUID employeeId;
    private String date;
}
