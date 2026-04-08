package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "shift_master_v3")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ShiftMasterV3 extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "shift_code", unique = true, nullable = false)
    private String shiftCode;

    @Column(name = "shift_name", nullable = false)
    private String shiftName;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(name = "grace_in_time")
    private Integer graceInTime; // in minutes

    @Column(name = "grace_out_time")
    private Integer graceOutTime; // in minutes

    @Column(name = "min_hours_full_day")
    private Double minHoursFullDay;

    @Column(name = "min_hours_half_day")
    private Double minHoursHalfDay;

    @Column(name = "is_night_shift")
    private Boolean isNightShift;
}
