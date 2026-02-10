package com.app.employeedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceFilterSearchDTO {
    private String year;
    private String month;
    private Integer pageSize;
    private Integer pageNo;
    private Integer yearA;
    private Integer monthA;


}
