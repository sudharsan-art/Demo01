package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEnrolledCourseCompletedViewDto {
    private String weekName;
    private List<String> topicName;
}
