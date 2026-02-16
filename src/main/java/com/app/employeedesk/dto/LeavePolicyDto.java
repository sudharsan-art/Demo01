package com.app.employeedesk.dto;

import lombok.Data;

@Data
public class LeavePolicyDto {
    private String id;
    private String role;
    private String leaveCode;
    private String leaveName;
    private Integer daysPerMonth;
    private Integer daysPerYear;
    private Boolean paid;
    private Boolean active;
}
