package com.app.employeedesk.dto;

import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.entity.AttendanceTiming;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceAttendanceTimingDto {
    public Attendance attendance;
    public AttendanceTiming attendanceTiming;
}
