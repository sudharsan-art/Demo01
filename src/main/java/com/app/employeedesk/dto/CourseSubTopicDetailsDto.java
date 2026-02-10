package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseSubTopicDetailsDto {
    private UUID id;
    private String subTopicName;
    private String topicName;
    private UUID topicId;

}
