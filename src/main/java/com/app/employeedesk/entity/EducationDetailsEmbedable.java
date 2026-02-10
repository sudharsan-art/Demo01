package com.app.employeedesk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class EducationDetailsEmbedable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "emp_id", insertable = false, updatable = false)
    private EmployeeBasicDetails employeeBasicDetails;
    @ManyToOne
    @JoinColumn(name = "qualification_id", insertable = false, updatable = false)
    private EducationQualification educationQualification;
}
