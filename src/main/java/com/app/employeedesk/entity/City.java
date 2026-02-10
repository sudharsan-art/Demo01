package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "city")
public class City extends AuditWithBaseEntity implements Serializable {
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "as_Name")
    private String asName;
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
    @Serial
    private static final long serialVersionUID = 1L;


}
