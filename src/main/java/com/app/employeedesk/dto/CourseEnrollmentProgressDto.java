package com.app.employeedesk.dto;

import lombok.*;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentProgressDto {
    private UUID employeeId;
    private String employeeCode;
    private String employeeName;
    private UUID courseEnrollmentId;
    private String startDate;

    public CourseEnrollmentProgressDto(UUID employeeId, String employeeCode, String employeeName, UUID courseEnrollmentId, LocalDate startDate) {
        String inputDateString = startDate.toString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String sDate = LocalDate.parse(inputDateString, inputFormatter).format(outputFormatter);
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.courseEnrollmentId = courseEnrollmentId;
        this.startDate = sDate;
    }




}
