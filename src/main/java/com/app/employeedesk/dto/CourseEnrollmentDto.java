package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CourseEnrollmentDto {
    private String courseId;
    private String employeeId;
    private String joiningDate;
}
