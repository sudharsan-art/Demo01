package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CourseReferenceLinkSubTopicDto {
    private UUID subTopicId;
    private UUID referenceLinkId;
    private String referenceLink;
}
