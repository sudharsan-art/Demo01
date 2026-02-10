package com.app.employeedesk.entity;
import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "education_qualification")
public class EducationQualification extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "qualification_name")
    private String qualificationName;
    @Column(name = "steam_yes_no")
    private String steamYesNo;
    @Column(name = "steam")
    private String steam;
    @OneToMany(mappedBy = "id.educationQualification")
    private List<EducationDetails> educationDetails;
}
