package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAttendanceDto {
    private UUID employeeId;
    private Long totalWorkingHrs;
}
