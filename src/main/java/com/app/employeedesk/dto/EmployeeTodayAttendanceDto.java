package com.app.employeedesk.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeTodayAttendanceDto {
    private UUID employeeId;
    private LocalDate attendanceDate;
}
