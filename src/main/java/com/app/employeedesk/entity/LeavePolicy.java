package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "leave_policy", uniqueConstraints = {@UniqueConstraint(name = "uk_employee_leave_code", columnNames = {"employee_id", "leave_code"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeavePolicy extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "role", length = 50)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id", nullable = false)
    private LeaveMaster leave;

    @Column(name = "days_per_month")
    private Integer daysPerMonth;

    @Column(name = "days_per_year")
    private Integer daysPerYear;

    @Column(name = "is_paid", nullable = false)
    private Boolean paid;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private UserDetails employee;

}
