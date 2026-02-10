package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeekOfDto {
    private String day;
    private int weekNo;
}
