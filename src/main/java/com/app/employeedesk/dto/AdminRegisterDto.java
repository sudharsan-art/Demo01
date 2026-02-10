package com.app.employeedesk.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
