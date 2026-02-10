package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubTopicOutputDto {
    private UUID subTopicId;
    private String subTopicName;
    private String description;
    private Double duration;
}
