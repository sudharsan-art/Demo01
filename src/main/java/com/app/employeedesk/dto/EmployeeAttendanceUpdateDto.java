package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAttendanceUpdateDto {
    private UUID id;
    private String date;
    private String inTime;
    private String outTime;
    private String reason;
    private String updateField;
    private String employeeName;
    private String updateStatus;
    private String appliedDate;
    private String adminRemark;
    private String empCode;
    private String actualInTime;
    private String actualOutTime;
    private UUID employeeBasicDetails;

}
