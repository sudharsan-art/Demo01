package com.app.employeedesk.enumeration;

import lombok.Getter;

@Getter
public enum LeaveType {
    EMERGENCY("Emergency"),
    PERSONAL("Personal"),
    MEDICAL_EMERGENCY("Medical emergency"),
    OTHERS("Others");
    private final String leave;
    LeaveType(String name) {
        this.leave=name;
    }
}
