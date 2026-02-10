package com.app.employeedesk.entity;
import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "skills_for_employee")
public class EmployeeSkillsForEmployee extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
    @OneToOne
    @JoinColumn(name = "emp_id")
    private EmployeeBasicDetails employeeBasicDetails;
    @Column(name = "skill_id")
    private UUID skillId;
    @Column(columnDefinition = "json" ,name = "skill_object" )
    private String employeeSkillsJson;
}
