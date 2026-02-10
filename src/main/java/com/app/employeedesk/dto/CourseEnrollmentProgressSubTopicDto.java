package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.CourseStatus;
import com.app.employeedesk.enumeration.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentProgressSubTopicDto {
    private UUID subTopicId;
    private String subTopicName;
    private CourseStatus status;
    private Integer weekNumber;
}
