package com.app.employeedesk.dto;

import com.app.employeedesk.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionListDto {
    private String id;
    private String employeeCode;
    private String employeeName;
    private String startTime;
    private String endTime;
    private String date;

    public PermissionListDto(UUID id, String employeeCode, String employeeName, LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.id = id.toString();
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.startTime = DateUtil.parseStringTme(startTime) ;
        this.endTime = DateUtil.parseStringTme(endTime);
        this.date = DateUtil.parseString(date);
    }

}
