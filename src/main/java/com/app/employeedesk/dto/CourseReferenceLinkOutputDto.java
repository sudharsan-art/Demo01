package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseReferenceLinkOutputDto {
    private UUID courseId;
    List<CourseReferenceLinkTopicOutputDto> courseReferenceLinkTopicOutputDto;
}
