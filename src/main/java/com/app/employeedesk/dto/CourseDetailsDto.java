package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailsDto {
    private UUID id;
    private String name;
    private Double duration;
    private String description;
}
