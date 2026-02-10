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
@Table(name = "course_sub_topic")
public class CourseSubTopicDetails extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name="name")
    private String subTopicName;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "duration")
    private Double duration;
    @OneToMany(mappedBy = "courseSubTopicDetails",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<CourseSubTopicWeekDurationDetails> subTopicWeekDurationDetails;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private CourseTopicDetails courseTopicDetails;
    @OneToMany(mappedBy = "courseSubTopicDetails")
    private List<CourseMaterials> courseMaterials;
    @OneToMany(mappedBy = "courseSubTopicDetails")
    private List<CourseTasksDetails> courseTasksDetails;
    @OneToOne(mappedBy = "courseSubTopicDetails",fetch = FetchType.EAGER)
    private CourseReferenceLink courseReferenceLinks;
    @OneToOne(mappedBy = "courseSubTopicDetails")
    private EmployeeCourseSubTopicDetails employeeCourseSubTopicDetails;

}
