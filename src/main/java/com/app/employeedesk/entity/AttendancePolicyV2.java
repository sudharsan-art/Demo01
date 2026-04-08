package com.app.employeedesk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalTime;

@Entity
@Table(name = "tbl_attendance_policy_v2")
@Getter
@Setter
public class AttendancePolicyV2 {

    @Id
    private String id;

    private LocalTime officeStartTime;
    private LocalTime officeEndTime;
    private int lateGraceMinutes;
    private int maxLateAllowed;
}

