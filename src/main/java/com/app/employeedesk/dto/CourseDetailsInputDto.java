package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailsInputDto {
    private String courseId;
    private String courseName;
    private String description;
    private List<TopicOutputDto> topicDetailsInputDto;
}
