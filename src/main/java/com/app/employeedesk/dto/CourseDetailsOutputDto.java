package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailsOutputDto {
    private UUID id;
    private String courseName;
}
