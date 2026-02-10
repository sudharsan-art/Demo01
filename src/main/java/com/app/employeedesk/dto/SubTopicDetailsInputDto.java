package com.app.employeedesk.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubTopicDetailsInputDto {
    private String subTopicName;
    private String description;
}
