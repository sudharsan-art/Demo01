package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducationalDetailsDto {
    private EducationDetailsEmbedableDto educationDetailsEmbedableDto;
    private UUID employeeId;
    private UUID educationId;
    private String institutionName;
    private String scoreType;
    private Double score;
    private String fromDate;
    private String toDate;
    private EmployeeBasicDetailsDto employeeBasicDetails;
}
