package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Table(name = "employee_week_off_v3")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class EmployeeWeekOffV3 extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserDetails employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "week_number")
    private Integer weekNumber; // 0 for "Every week", 1-5 for specific weeks

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo; // Nullable for indefinite week-offs
}
