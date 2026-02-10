package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private String empId;
    private String workType;
}
