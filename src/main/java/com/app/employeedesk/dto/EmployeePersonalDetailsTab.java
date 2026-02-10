package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeAddress;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeePersonalDetailsTab {
    private EmployeeBasicDetailsDto employeeBasicDetailsDto;
    private EmployeeAddressDto employeeAddressDto;
    private UserDetailsDto userDetailsDto;

}
