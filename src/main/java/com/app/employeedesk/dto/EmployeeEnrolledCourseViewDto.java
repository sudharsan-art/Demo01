package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEnrolledCourseViewDto {
    private String courseId;
    private String courseName;
    private String courseDuration;
    private String courseDescription;

}
