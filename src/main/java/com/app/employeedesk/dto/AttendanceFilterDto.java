package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceFilterDto {
    private int year;
    private int month;
}
