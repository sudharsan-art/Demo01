package com.app.employeedesk.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HolidayPresentComboDto {
    private String name;
    private String holidayDate;
    private String holidayType;
    private Integer totalHoursWorked;
    private String workSummary;

}
