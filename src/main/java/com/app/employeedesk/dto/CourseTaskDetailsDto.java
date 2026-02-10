package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseTaskDetailsDto {
    private UUID subTopicId;
    private String taskName;
}
