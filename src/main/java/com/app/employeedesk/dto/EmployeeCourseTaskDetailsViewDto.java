package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.CourseStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCourseTaskDetailsViewDto {
   private UUID taskId;
   private String taskName;
   private CourseStatus taskStatus;
   private byte[] taskImage;
}
