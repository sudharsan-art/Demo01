package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeShiftEntity;
import lombok.*;

import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWeekOffDto {
    private UUID id;
    private String day;
    private Integer weekNo;
    private String dayStatus;

}
