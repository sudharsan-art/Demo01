package com.app.employeedesk.dto;

import lombok.*;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateIntimeOutTime {
    private LocalTime inTime;
    private LocalTime outTime;
}
