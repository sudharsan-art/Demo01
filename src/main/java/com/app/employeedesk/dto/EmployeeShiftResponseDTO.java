package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class EmployeeShiftResponseDTO {
    private String employeeId;
    private String employeeName;

    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDate effectiveDate;
}
