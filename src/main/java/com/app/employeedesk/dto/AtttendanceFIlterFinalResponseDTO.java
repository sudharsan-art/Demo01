package com.app.employeedesk.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtttendanceFIlterFinalResponseDTO {
    private List<AttendanceFilterResponseDTO> attendanceFilterResponseDTOList;
    private Long noOfDaysPresent;
    private Long noOfDaysAbsent;


}
