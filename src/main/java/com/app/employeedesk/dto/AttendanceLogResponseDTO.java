package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttendanceLogResponseDTO {
    private String employeeId;
    private String employeeName;

    private LocalDate attendanceDate;
    private LocalDateTime punchTime;
    private String type;
}
