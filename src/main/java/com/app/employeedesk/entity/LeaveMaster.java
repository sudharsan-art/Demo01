package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "leave_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "leave_code", nullable = false, unique = true)
    private String leaveCode;

    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    @Column(name = "is_active", nullable = false)
    private Boolean active;
}
