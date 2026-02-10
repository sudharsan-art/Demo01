package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ComboOffResponseDto {
    private LocalDate date;
    private String holidayType;
    private Integer totalHoursWorked;
}
