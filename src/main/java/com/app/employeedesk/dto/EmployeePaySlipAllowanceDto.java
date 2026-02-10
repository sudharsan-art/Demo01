package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePaySlipAllowanceDto {
    private UUID allowanceId;
    private Double percentage;
    private String reason;
    private Double amount;
}
