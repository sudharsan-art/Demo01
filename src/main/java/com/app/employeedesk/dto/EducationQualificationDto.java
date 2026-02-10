package com.app.employeedesk.dto;
import com.app.employeedesk.entity.EducationDetails;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EducationQualificationDto {
    private UUID id;
    private String qualificationName;
    private String steamYesNo;
    private String steam;
    private List<EducationalDetailsDto> educationDetails;
}
