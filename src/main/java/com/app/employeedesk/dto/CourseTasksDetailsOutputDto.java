package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseTasksDetailsOutputDto {
    private UUID subTopicId;
    private UUID taskId;
    private String taskName;
    private byte[] taskImage;

}
