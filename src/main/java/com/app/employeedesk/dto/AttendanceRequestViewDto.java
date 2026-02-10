package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.UpdateStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequestViewDto {
    private LocalDate date;
    private LocalTime inTime;
    private LocalTime outTime;
    private String reason;
    private UpdateStatus status;
    private String remark;
}
