package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentProgressDetailsDto {
    private String courseName;
    private Map<String,List<CourseEnrollmentProgressTopicDto>> progressList;

}
