package com.app.employeedesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeShiftAssignDTO {
    private String employeeId;
    private String shiftId;
    private LocalDate effectiveDate;
}
