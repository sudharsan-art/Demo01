package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseTopicAndSubTopicDto {
    private UUID weekId;
    private List<TopicDetailsInputDto> topicDetailsInputDto;
}
