package com.app.employeedesk.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDto {
    private String id;
    private String date;
    private String reason;

    public HolidayDto(UUID id, LocalDate date, String reason) {

        String inputDateString = date.toString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String holiday = LocalDate.parse(inputDateString, inputFormatter).format(outputFormatter);

        this.id = id.toString();
        this.date =holiday;
        this.reason = reason;
    }

}
