package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "leave_request_v2")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestV2 extends AuditWithBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserDetails employee;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private LocalDate fromDate;
    private LocalDate toDate;

    private int numberOfDays;

    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
}
