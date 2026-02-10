package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class EmployeePaySlipDeductionDto {
    private UUID deductionId;
    private Double percentage;
    private String reason;
    private Double amount;
}
