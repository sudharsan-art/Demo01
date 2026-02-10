package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseTopicSubTopicInputDto {
    private UUID courseId;
    List<CourseTopicAndSubTopicDto> courseTopicAndSubTopicDtoList;
}
