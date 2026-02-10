package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceUpdateDto {
    private String empId;
    private String date;
    private String checkIn;
    private String checkout;
}
