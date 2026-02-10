package com.app.employeedesk.enumeration;

import lombok.Getter;

@Getter
public enum LeaveStatus {
    ACCEPT("Accept"),
    REJECT("Reject"),
    PENDING("Pending");
    private final String status;
    LeaveStatus(String status) {
        this.status=status;
    }
}
