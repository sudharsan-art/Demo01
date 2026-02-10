package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table(name="course_topic")
public class CourseTopicDetails extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name="name")
    private String topicName;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "duration")
    private Double duration;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseDetails courseDetails;
    @OneToMany(mappedBy = "courseTopicDetails",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CourseSubTopicDetails> courseSubTopicDetails;
    @OneToOne(mappedBy = "courseTopicDetails")
    private EmployeeCourseTopicDetails employeeCourseTopicDetails;
}
