package com.app.employeedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftMasterV3Dto {
    private UUID id;
    private String shiftCode;
    private String shiftName;
    private String startTime;
    private String endTime;
    private String breakStartTime;
    private String breakEndTime;
    private Integer graceInTime;
    private Integer graceOutTime;
    private Double minHoursFullDay;
    private Double minHoursHalfDay;
    private Boolean isNightShift;
}
