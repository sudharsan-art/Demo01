package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestV2Dto {
    private LeaveType leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
}
