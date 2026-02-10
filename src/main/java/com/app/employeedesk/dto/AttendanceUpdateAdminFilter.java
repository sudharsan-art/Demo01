package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceUpdateAdminFilter {
    private String status;
    private String month;
    private String year;
}
