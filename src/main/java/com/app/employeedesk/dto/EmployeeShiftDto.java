package com.app.employeedesk.dto;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeShiftDto {
    private UUID id;
    private String shiftName;
    private String shiftStartTime;
    private String shiftEndTime;
    private String firstHalfStartTime;
    private String firstHalfEndTime;
    private String secoundHalfStartTime;
    private String secoundHalfEndTime;
    private String remarks;
    private List<EmployeeWeekOffDto> employeeWeekOff;
}
