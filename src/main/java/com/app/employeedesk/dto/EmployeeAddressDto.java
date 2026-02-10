package com.app.employeedesk.dto;

import com.app.employeedesk.entity.City;
import com.app.employeedesk.entity.Country;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.State;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeAddressDto {
    private UUID id;
    private String doorNo;
    private String street;
    private String landmark;
    private Country country;
    private State state;
    private City city;
    private Integer postalCode;
    private EmployeeBasicDetails employeeBasicDetails;
}
