package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseDetailsInputViewDto {
    private UUID courseId;
    private String courseName;
    private String description;
    private Double duration;
    private List<CourseTopicDetailsViewDto> courseTopicDetailsViewDto;
}
