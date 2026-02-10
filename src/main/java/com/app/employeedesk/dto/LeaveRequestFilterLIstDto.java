package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestFilterLIstDto {
    private UUID leaveId;
    private String employeeCode;
    private String employeeName;
    private LocalDate fromDate;
    private LocalDate endDate;
    private LeaveType leaveType;
}
