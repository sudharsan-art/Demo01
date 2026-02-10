package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserDetailsDto {
    private UUID id;
    private String name;
    private String userName;
    private String phoneNumber;
    private String password;
    private UserType role;
    private Status status;
    private EmployeeBasicDetails employeeBasicDetails;
}
