package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class AttendancePolicyDTO {
    private String id;

    private LocalTime officeStartTime;
    private LocalTime officeEndTime;

    private int lateGraceMinutes;
    private int maxLateAllowed;
}
