package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_shift")
public class EmployeeShiftEntity extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
    @Column(name = "shift_name")
    private String shiftName;
    @Column(name = "shift_start_time")
    private LocalTime shiftStartTime;
    @Column(name = "shift_end_time")
    private LocalTime shiftEndTime;
    @Column(name = "first_half_start_time")
    private LocalTime firstHalfStartTime;
    @Column(name = "first_half_end_time")
    private LocalTime firstHalfEndTime;
    @Column(name = "secound_half_start_time")
    private LocalTime secoundHalfStartTime;
    @Column(name = "secound_half_end_time")
    private LocalTime secoundHalfEndTime;
    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "shiftid",cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<EmployeeWeekOff> employeeWeekOff;
    @OneToMany(mappedBy = "employeeShiftEntity" ,cascade = CascadeType.ALL)
    private List<EmployeeBasicDetails> employeeBasicDetails;


}
