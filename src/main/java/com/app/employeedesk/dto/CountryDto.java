package com.app.employeedesk.dto;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDto {
    private UUID id;
    private String name;
    private String phoneCode;
    private String asName;
    private List<StateDto> stateList;

}
