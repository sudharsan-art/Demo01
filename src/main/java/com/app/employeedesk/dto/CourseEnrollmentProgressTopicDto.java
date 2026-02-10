package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentProgressTopicDto {
    private UUID topicId;
    private String topicName;
    private List<CourseEnrollmentProgressSubTopicDto> courseEnrollmentProgressSubTopic;

}
