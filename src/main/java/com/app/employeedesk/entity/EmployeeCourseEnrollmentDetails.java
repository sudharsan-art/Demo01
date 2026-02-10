package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_course_enrollment_details")
public class EmployeeCourseEnrollmentDetails extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name = "emp_course_status")
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(name = "joining_date")
    private LocalDate joiningDate;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "total_weeks_completed")
    private Integer totalWeeksCompleted;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseDetails courseDetails;
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private EmployeeBasicDetails employeeBasicDetails;
    @OneToMany(mappedBy = "employeeCourseEnrollmentDetails",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<EmployeeCourseTopicDetails> employeeCourseTopicDetails;
}
