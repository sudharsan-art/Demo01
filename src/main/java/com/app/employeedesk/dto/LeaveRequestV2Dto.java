package com.app.employeedesk.dto;


import com.app.employeedesk.enumeration.LeaveTypeV2;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.app.employeedesk.enumeration.HalfDaySession;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeaveRequestV2Dto {
    @NotBlank(message = "Leave code is required")
    private String leaveCode;

    @NotNull(message = "From date is required")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    private LocalDate toDate;

    private Boolean halfDay;

    private String halfDaySession;

    @NotBlank(message = "Reason is required")
    private String reason;
}
