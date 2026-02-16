package com.app.employeedesk.dto;

import lombok.Data;

@Data
public class LeaveRequestCreateV2Dto {
    private String fromDate;
    private String endDate;
    private String leaveCode; // from role config
    private String reason;
}
