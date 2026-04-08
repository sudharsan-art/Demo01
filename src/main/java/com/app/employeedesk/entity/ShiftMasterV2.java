package com.app.employeedesk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalTime;

@Entity
@Table(name = "shift_master_v2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftMasterV2 {
    @Id
    @Column(length = 36)
    private String id;

    private String shiftName;

    private LocalTime startTime;
    private LocalTime endTime;
}
