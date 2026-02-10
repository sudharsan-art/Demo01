package com.app.employeedesk.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkingTime {
    private int hours;
    private long minutes;
}
