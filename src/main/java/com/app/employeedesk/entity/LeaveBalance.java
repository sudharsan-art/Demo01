package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "leave_balance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserDetails employee;

    @Column(nullable = false)
    private String leaveCode;

    private double totalAllocated;
    private double usedDays;
    private double remainingDays;
}

