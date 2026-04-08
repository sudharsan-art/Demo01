package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_attendance_v2",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "attendance_date"}))
@Getter
@Setter
public class AttendanceV2 {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private UserDetails employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "punch_in_time")
    private LocalDateTime punchInTime;

    @Column(name = "punch_out_time")
    private LocalDateTime punchOutTime;

    private Double workHours;

    @Column(name = "actual_hours")
    private Double actualHours; // Total time between IN and OUT

    @Column(name = "late_count")
    private Integer lateCount = 0;

    @Column(name = "early_leaving")
    private Boolean earlyLeaving = false;

    @Column(name = "buffer_warning")
    private Boolean bufferWarning = false;

    private String status;
}
