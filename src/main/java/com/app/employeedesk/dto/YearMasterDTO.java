package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class YearMasterDTO {
    private String id;
    private String yearName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
