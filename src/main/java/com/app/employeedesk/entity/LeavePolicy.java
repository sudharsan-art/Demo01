package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "leave_policy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeavePolicy extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    private UUID id;

    @Column(name="role",nullable = false,length = 50)
    private String role;

    @Column(name = "leave_code", nullable = false, length = 50)
    private String leaveCode; // CL, SL, EL, etc

    @Column(name = "leave_name", nullable = false, length = 100)
    private String leaveName; // Casual Leave, Sick Leave

    @Column(name = "days_per_month")
    private Integer daysPerMonth;

    @Column(name = "days_per_year")
    private Integer daysPerYear;

    @Column(name = "is_paid", nullable = false)
    private Boolean paid;

    @Column(name = "is_active", nullable = false)
    private Boolean active;


}
