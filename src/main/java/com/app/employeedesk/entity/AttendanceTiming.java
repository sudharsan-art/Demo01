package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
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
@Table(name = "attendance_timing")
public class AttendanceTiming extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    @Column(name = "in_time")
    private LocalTime inTime;
    @Column(name = "out_time")
    private LocalTime outTime;
    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendances;
}
