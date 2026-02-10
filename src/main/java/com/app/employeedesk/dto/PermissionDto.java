package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private String id;
    private String startTime;
    private String endTime;
    private String status;
    private String date;
    private String reason;
    private String employeeId;
}
