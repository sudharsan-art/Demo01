package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAttendanceDto {
    private UUID empId;
    private String date;
    private String outTime;
    private String inTime;
    private String status;
}
