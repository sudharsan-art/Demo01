package com.app.employeedesk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OdRequestDto {
    private String employeeId;
    private String odDate;
    private String reason;
    private String location;
}

