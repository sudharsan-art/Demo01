package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmployeeLeaveConfigViewDto {
    private String employeeId;
    private String employeeName;
    private String role;
    private List<LeavePolicyDto> allowedLeaves;
}
