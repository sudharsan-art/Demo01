package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.DepartmentType;
import com.app.employeedesk.enumeration.DesignationType;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBasicDetailsViewDto {
    private UUID employeeId;
    private String employeeName;
    private String employeeCode;
    private DepartmentType department;
    private DesignationType designation;


}
