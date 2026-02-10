package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubqueryTemporaryTableDTO {
    private Integer experience;
    private EmployeeBasicDetails employeeBasicDetails;
}
