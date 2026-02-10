package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_course_topic_details")
public class EmployeeCourseTopicDetails extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseStatus status;
    @ManyToOne
    @JoinColumn(name = "employee_course_enrollment_id")
    private EmployeeCourseEnrollmentDetails employeeCourseEnrollmentDetails;
    @OneToOne
    @JoinColumn(name = "course_topic_id")
    private CourseTopicDetails courseTopicDetails;
    @OneToMany(mappedBy = "employeeCourseTopicDetails",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<EmployeeCourseSubTopicDetails> employeeCourseSubTopicDetails;

}
