package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDto {
    private String id;
    private String leaveType;
    private String reason;
    private String fromDate;
    private String leaveStatus;
    private String startDateStatus;
    private String endDateStatus;
    private String endDate;
    private String leaveDayType;
    private String employeeID;
}
