package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDetailsInputDto {
    private String topicName;
    private String description;
    private List<SubTopicDetailsInputDto> subTopicDetailsInputDto;
}
