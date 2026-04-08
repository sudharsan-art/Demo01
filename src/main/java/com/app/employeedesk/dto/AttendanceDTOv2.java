package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttendanceDTOv2 {
    private String employeeId;
    private String employeeName;

    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;

    private Integer lateCount;
    private Double workHours;
    private String status;
}
