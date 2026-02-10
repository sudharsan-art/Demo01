package com.app.employeedesk.dto;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.enumeration.WorkMode;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBasicDetailsDto {

    private UUID id;
    private String profileImage;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String panCardNumber;
    private String fatherName;
    private String motherName;
    private String desigination;
    private String department;
    private LocalDate dateOfJoining;
    private String employeecode;
    private WorkMode workMode;
    private UserDetails userDetailsId;
}
