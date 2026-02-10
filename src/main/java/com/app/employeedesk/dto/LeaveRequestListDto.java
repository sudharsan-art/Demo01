package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.LeaveType;
import com.app.employeedesk.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LeaveRequestListDto {
    private String leaveId;
    private String employeeCode;
    private String employeeName;
    private String fromDate;
    private String endDate;
    private String leaveType;

    public LeaveRequestListDto(UUID leaveId, String employeeCode, String employeeName, LocalDate fromDate, LocalDate endDate, LeaveType leaveType) {
        this.leaveId = leaveId.toString();
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.fromDate = DateUtil.parseString(fromDate);
        this.endDate = DateUtil.parseString(endDate);
        this.leaveType = String.valueOf(leaveType);
    }
}
