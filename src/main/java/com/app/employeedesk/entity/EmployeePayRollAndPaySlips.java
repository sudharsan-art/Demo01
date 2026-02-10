package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_payroll_payslip")
public class EmployeePayRollAndPaySlips extends AuditWithBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    @Column(name = "basic_salary")
    private Double basicSalary;
    @Column(name = "net_salary")
    private Double netSalary;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @OneToOne
    @JoinColumn(name = "emp_id")
    private EmployeeBasicDetails employeeBasicDetails;
    @OneToMany(mappedBy = "employeePayRollAndPaySlips",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<EmployeePaySlipAllowances> employeePaySlipAllowances;
    @OneToMany(mappedBy = "employeePayRollAndPaySlips",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<EmployeePaySlipDeductions> employeePaySlipDeductions ;



}
