package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.PunchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendancePunchDTO {
    private String employeeId;
    private PunchType type;
}
