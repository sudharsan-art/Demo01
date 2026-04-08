package com.app.employeedesk.mapper;

import com.app.employeedesk.dto.AttendanceDTOv2;
import com.app.employeedesk.dto.AttendanceResponseDTO;
import com.app.employeedesk.entity.AttendanceV2;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public static AttendanceResponseDTO toDTO(AttendanceV2 a) {

        return AttendanceResponseDTO.builder()
                .employeeId(a.getEmployee().getId().toString())
                .employeeName(a.getEmployee().getName())
                .attendanceDate(a.getAttendanceDate())
                .punchInTime(a.getPunchInTime())
                .punchOutTime(a.getPunchOutTime())
                .workHours(a.getWorkHours())
                .lateCount(a.getLateCount())
                .status(a.getStatus())
                .build();
    }

    public static AttendanceDTOv2 toTodayDTO(AttendanceV2 a) {
        return AttendanceDTOv2.builder()
                .employeeId(a.getEmployee().getId().toString())
                .employeeName(a.getEmployee().getName())
                .punchInTime(a.getPunchInTime())
                .punchOutTime(a.getPunchOutTime())
                .lateCount(a.getLateCount())
                .workHours(a.getWorkHours())
                .status(a.getStatus())
                .build();
    }
}