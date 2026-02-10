package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.CourseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class EmployeeCourseSubTopicDetailsDto {
    private UUID subtopicId;
    private String subtopicName;
    private CourseStatus status;
}
