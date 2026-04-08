package com.app.employeedesk.repo;

import com.app.employeedesk.entity.AttendanceV2;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceV2Repository extends JpaRepository<AttendanceV2, String> {

    Optional<AttendanceV2> findByEmployeeAndAttendanceDate(UserDetails emp, LocalDate date);

    boolean existsByEmployeeAndAttendanceDate(UserDetails emp, LocalDate date);

    List<AttendanceV2> findByEmployee(UserDetails employee);
}
