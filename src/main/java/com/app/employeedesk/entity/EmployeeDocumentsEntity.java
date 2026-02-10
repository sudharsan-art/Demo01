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
@Table(name = "employee_documents")
public class EmployeeDocumentsEntity extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "document_name")
    private String documentName;
    @Lob
    @Column(name = "files")
    private byte[] files;
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private EmployeeBasicDetails employeeBasicDetails;



}
