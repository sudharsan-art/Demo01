package com.app.employeedesk.repo;

import com.app.employeedesk.dto.DateIntimeOutTime;
import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.entity.AttendanceTiming;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceTimingRepository extends JpaRepository<AttendanceTiming, UUID> {
    @Query(value = "select at from AttendanceTiming at where at.attendances.id=:id and at.outTime is null ")
  Optional<AttendanceTiming> CheckNullHereInCheckOutColumn(UUID id);


    @Query(value = "select at from AttendanceTiming  at where at.attendances.id=:id ")
    List<AttendanceTiming> findByTotalhours(UUID id);

    @Query( "select  new com.app.employeedesk.dto.DateIntimeOutTime(min(a.inTime),max(a.outTime)) from AttendanceTiming a inner join Attendance at on at=a.attendances where at.date=:date and at.employeeId=:empId")
    Optional<DateIntimeOutTime> findInTimeOutTime(LocalDate date, UUID empId);
}
