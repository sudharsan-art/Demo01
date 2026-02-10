package com.app.employeedesk.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminFilterResponseDTO {
    private String employeeId;
    private String name;
    private String role;
    private Integer experience;
    private String contactNo;

}
