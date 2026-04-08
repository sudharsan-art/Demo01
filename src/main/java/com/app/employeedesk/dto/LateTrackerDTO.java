package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LateTrackerDTO {
    private String employeeId;
    private String employeeName;

    private int month;
    private int year;

    private int totalLateCount;
    private int deductedLeaveCount;
}
