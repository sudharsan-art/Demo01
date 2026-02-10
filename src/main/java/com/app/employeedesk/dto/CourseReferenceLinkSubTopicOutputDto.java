package com.app.employeedesk.dto;

import com.app.employeedesk.entity.CourseReferenceLink;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
public class CourseReferenceLinkSubTopicOutputDto {
    private UUID subTopicId;
    private String subTopicName;
    private UUID referenceId;
    private String referenceLink;



}
