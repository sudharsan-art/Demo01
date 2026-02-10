package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.UpdateStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_attendance_update")
public class AttendanceUpdate extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    @Column(name = "att_date")
    private LocalDate attendanceDate;
    @Column(name = "in_time")
    private LocalTime inTime;
    @Column(name = "out_time")
    private LocalTime outTime;
    @Column(name = "reason")
    private String reason;
    @Column(name = "approve_status")
    @Enumerated(EnumType.STRING)
    private UpdateStatus updateStatus;
    @ManyToOne
    @JoinColumn(name="emp_id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private EmployeeBasicDetails employeeBasicDetails;
    @Column(name = "update_field")
    private String updateField;
    @Column(name = "applied_date")
    private LocalDate appliedDate;
    @Column(name = "admin_remark")
    private String adminRemark;
    @Column(name = "emp_code")
    private String empCode;
    @Column(name = "actual_in_time")
    private LocalTime actualInTime;
    @Column(name = "actual_out_time")
    private LocalTime actualOutTime;



}
