package com.app.employeedesk.repo;

import com.app.employeedesk.entity.AttendanceLogV2;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceLogV2Repository extends JpaRepository<AttendanceLogV2, UUID> {
    List<AttendanceLogV2> findByEmployeeAndAttendanceDateOrderByPunchTime(
            UserDetails employee, LocalDate date
    );
}
