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
@Table(name = "employee_address_details")
public class EmployeeAddress extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    @Column(name = "door_no")
    private String doorNo;
    @Column(name = "street")
    private String street;
    @Column(name = "landmark")
    private String landmark;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;
    @Column(name = "postal_code")
    private Integer postalCode;
    @OneToOne
    @JoinColumn(name = "emp_id")
    private  EmployeeBasicDetails employeeBasicDetails;

}
