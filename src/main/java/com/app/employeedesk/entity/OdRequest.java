package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_employee_od_request")
@Getter
@Setter

public class OdRequest {

        @Id
        @GeneratedValue (strategy = GenerationType.UUID)
        private String id;

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private EmployeeBasicDetails employee;

        @Column(name = "od_date", nullable = false)
        private LocalDate odDate;

        private String reason;

        private String location;

        private String status;

        @Column(name = "applied_on")
        private LocalDate appliedOn;

        @Column(name = "approved_by")
        private String approvedBy;

        @Column(name = "approved_on")
        private LocalDate approvedOn;
    }

