package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkillsForEmployeeDto {

    private UUID id;
    private UUID employeeBasicDetailsDto;
    private UUID skillId;
    private EmployeeSkillsDto employeeSkillsJson;
}
