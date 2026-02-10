package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;

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
@Table(name = "state")
public class State extends AuditWithBaseEntity implements Serializable {
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "as_Name")
    private String asName;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToMany(mappedBy = "state")
    private List<City> cityList;
    @Serial
    private static final long serialVersionUID = 1L;
}
