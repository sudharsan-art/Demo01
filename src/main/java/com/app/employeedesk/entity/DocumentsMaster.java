package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "documents_master")
public class DocumentsMaster extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name = "document_name")
    private String documentsName;
    @Column(name = "mandatory_yes_or_no")
    private String documentStatus;
}
