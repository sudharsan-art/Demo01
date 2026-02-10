package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "country")
public class Country  extends AuditWithBaseEntity implements Serializable {
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_code")
    private String phoneCode;
    @Column(name = "as_Name")
    private String asName;
    @OneToMany(mappedBy = "country")
    private List<State> stateList;
    @Serial
    private static final long serialVersionUID = 1L;
}
