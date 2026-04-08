package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_late_tracker_v2",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "month", "year"}))
@Getter
@Setter
public class LateTrackerV2 {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private UserDetails employee;

    private int month;
    private int year;

    @Column(name = "deducted_leave_count")
    private int deductedLeaveCount;

    private int totalLateCount;
}
