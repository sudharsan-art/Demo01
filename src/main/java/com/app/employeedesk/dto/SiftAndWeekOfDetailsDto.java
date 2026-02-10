package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeWeekOff;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiftAndWeekOfDetailsDto {
    private LocalTime firstHalfStartTime;
    private LocalTime firstHalfEndTime;
    private LocalTime secondHalfStartTime;
    private LocalTime secondHalfEndTime;
    private LocalTime siftStartTime;
    private LocalTime siftEndTime;
    private UUID siftId;
}
