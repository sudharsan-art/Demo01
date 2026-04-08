package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveMasterDto {
    private UUID id;
    private String leaveCode;
    private String leaveName;

}
