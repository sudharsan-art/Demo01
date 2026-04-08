package com.app.employeedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShiftAssignmentV3Dto {
    private UUID id;
    private UUID employeeId;
    private UUID shiftId;
    private LocalDate fromDate;
    private LocalDate toDate;
}
