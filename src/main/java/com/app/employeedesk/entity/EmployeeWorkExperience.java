package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_work_experience_details")
public class EmployeeWorkExperience extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_location")
    private String companyLocation;
    @Column(name="experience")
    private Integer experience;
    @Column(name = "role")
    private String role;
    @Column(name="date_of_joining")
    private Date dateOfJoining;
    @Column(name="date_of_releaving")
    private Date dateOfReleaving;
    @ManyToOne
    @JoinColumn(name="emp_id")
    private EmployeeBasicDetails employeeBasicDetails;

}
