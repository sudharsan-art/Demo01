package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.AttendanceStatus;
import com.app.employeedesk.enumeration.ReasonStatus;
import com.app.employeedesk.enumeration.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Table(name = "attendance")
public class Attendance extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_status")
    private ReasonStatus reasonStatus;

    @Column(name = "in_time")
    private LocalTime inTime;

    @Column(name = "out_time")
    private LocalTime outTime;

    @Column(name = "totel_hours")
    private Integer hours;

    @Column(name = "minutes")
    private Long minutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode")
    private WorkMode workMode;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "employee_id")
    private UUID employeeId;


}

