package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TopicOutputDto {
    private UUID topicId;
    private String topicName;
    private String description;
    private List<SubTopicOutputDto> subTopicDetailsInputDto;
}
