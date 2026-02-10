package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import lombok.*;

import java.util.Date;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkExperienceDto {
    private UUID id;
    private String companyName;
    private String companyLocation;
    private Integer experience;
    private String role;
    private String dateOfJoining;
    private String  dateOfReleaving;
    private UUID employeeBasicDetailsId;
    private EmployeeBasicDetails employeeBasicDetails;

}
