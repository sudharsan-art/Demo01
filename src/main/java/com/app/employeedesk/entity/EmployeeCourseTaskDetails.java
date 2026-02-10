package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_course_task_details")
public class EmployeeCourseTaskDetails extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseStatus status;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "task_url")
    private String taskUrl;
    @Lob
    @Column(name = "snapshot")
    private byte[] snapshot;
    @Column(name = "submitted_on")
    private LocalDate submittedOn;
    @Column(name = "task_start_time")
    private LocalTime startTime;
    @ManyToOne
    @JoinColumn(name = "employee_course_sub_topic_id")
    private EmployeeCourseSubTopicDetails employeeCourseSubTopicDetails;
    @OneToOne
    @JoinColumn(name = "course_task_id")
    private CourseTasksDetails courseTasksDetails;
}
