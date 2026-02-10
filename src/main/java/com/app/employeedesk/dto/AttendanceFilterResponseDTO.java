package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.AttendanceStatus;

import com.app.employeedesk.enumeration.WorkMode;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceFilterResponseDTO {
    private LocalDate date;
    private String status;
    private String workMode;
    private java.lang.Integer totalWorkingHours;


    public AttendanceFilterResponseDTO(LocalDate date, AttendanceStatus status, WorkMode workMode, Integer totalWorkingHours) {
        this.date = date;
        this.status =status!=null?status.name():null;
        this.workMode =workMode!=null?workMode.name():null;
        this.totalWorkingHours = totalWorkingHours;

    }


}
