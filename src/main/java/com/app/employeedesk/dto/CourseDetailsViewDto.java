package com.app.employeedesk.dto;


import com.app.employeedesk.util.DateUtil;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailsViewDto {
    private String courseId;
    private String courseName;
    private String courseEnrollmentId;
    private String startDate;
    private String endDate;
    private int totalTopics;
    private int completedTopics;

    public CourseDetailsViewDto(UUID courseId, String courseName, UUID courseEnrollmentId,LocalDate startDate, LocalDate endDate) {
        this.courseId = courseId.toString();
        this.courseName = courseName;
        this.courseEnrollmentId = courseEnrollmentId.toString();
        this.startDate = DateUtil.parseString(startDate);
        this.endDate = DateUtil.parseString(endDate);
    }

    public void setTopicsDetails(int totalTopics, int completedTopics) {
        this.totalTopics = totalTopics;
        this.completedTopics = completedTopics;
    }
}
