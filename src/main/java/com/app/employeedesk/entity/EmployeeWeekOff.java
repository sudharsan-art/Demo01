package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_week_off")
public class EmployeeWeekOff extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
    @Column(name = "week_days")
    private String Day;
    @Column(name = "month_days_position")

    private Integer weekNo;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    @JsonIgnore
    private EmployeeShiftEntity shiftid;
    @Column(name = "day_status")
    private String dayStatus;

}
