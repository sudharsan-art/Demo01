package com.app.employeedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveFilterDto {
    private String date;
    private String leaveType;
    private String leaveStatus;
    private Integer pageNumber;
    private Integer pageSize;
}
