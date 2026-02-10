package com.app.employeedesk.dto;

import com.app.employeedesk.entity.Country;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class StateDto {
    private UUID  id;
    private String name;
    private String asName;
    private String countryName;
    private UUID country;


}
