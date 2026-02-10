package com.app.employeedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FindEmployeeNameDto {
    private UUID employeeId;
    private String employeeName;
    private String employeeCode;
}
