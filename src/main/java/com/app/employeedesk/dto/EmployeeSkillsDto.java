package com.app.employeedesk.dto;


import lombok.*;

import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkillsDto {
    private UUID id;
    private String skillName;
}
