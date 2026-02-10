package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.Status;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "course_details")
public class CourseDetails extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name="name")
    private String courseName;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "total_weeks")
    private Integer totalWeek;
    @OneToMany(mappedBy = "courseDetails",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CourseTopicDetails>courseTopicDetails;
    @OneToMany(mappedBy = "courseDetails")
    private List<EmployeeCourseEnrollmentDetails> employeeCourseEnrollmentDetails;
}
