package com.app.employeedesk.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducationDetailsEmbedableDto {
    private EmployeeBasicDetailsDto employeeBasicDetailsDto;
    private EducationQualificationDto educationQualificationDto;

}
