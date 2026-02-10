package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "holiday_master")
public class Holiday extends AuditWithBaseEntity {
    private UUID id;
    @Column(name = "date",unique = true)
    private LocalDate date;
    @Column(name = "reason")
    private String reason;
}
