package com.app.employeedesk.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminFilterRequestDTO {

    private String firstName;
    private String lastName;
    private Integer experience;
    private String previousCompanyName;
    private String skills;
}
