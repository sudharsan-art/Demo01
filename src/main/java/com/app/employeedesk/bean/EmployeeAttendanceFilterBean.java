package com.app.employeedesk.bean;

import java.time.LocalDate;

public interface EmployeeAttendanceFilterBean {
    LocalDate getDate();
    String getStatus();
     String getWorkMode();
     Integer getHours();

}
