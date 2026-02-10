package com.app.employeedesk.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentCompletedDto {
    private String employeeCode;
    private String employeeName;
    private String startDate;
    private String endDate;
    private String remarks;

    public CourseEnrollmentCompletedDto(String employeeCode, String employeeName, LocalDate startDate, LocalDate endDate, String remarks) {
        String inputStartDateString = startDate.toString();
        String inputEndDateString= endDate.toString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String sDate = LocalDate.parse(inputStartDateString, inputFormatter).format(outputFormatter);
        String eDate = LocalDate.parse(inputEndDateString, inputFormatter).format(outputFormatter);
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.startDate = sDate;
        this.endDate = eDate;
        this.remarks = remarks;
    }

}

