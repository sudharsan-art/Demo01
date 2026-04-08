package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceMonthlyReportDTO {
    private long presentDays;
    private long halfDays;
    private long absentDays;

    private double totalWorkHours;
}
