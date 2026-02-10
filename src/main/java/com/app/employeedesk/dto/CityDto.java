package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@Builder
public class CityDto {
    private UUID id;
    private String name;
    private String asName;
    private String stateName;
}
