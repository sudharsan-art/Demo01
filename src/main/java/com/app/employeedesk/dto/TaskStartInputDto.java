package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class TaskStartInputDto {
    private UUID taskId;
    private UUID employeeSubTopicId;
    private String url;
}
