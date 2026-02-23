package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.LeaveStatus;
import lombok.Data;

@Data
public class LeaveApprovalV2Dto {
    private LeaveStatus status;
}
