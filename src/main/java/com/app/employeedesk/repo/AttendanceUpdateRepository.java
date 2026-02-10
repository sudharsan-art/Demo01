package com.app.employeedesk.repo;
import com.app.employeedesk.entity.AttendanceUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceUpdateRepository extends JpaRepository<AttendanceUpdate, UUID> {

    @Query("select case when count(*)>0 then 'false' else 'true' end from AttendanceUpdate a where a.employeeBasicDetails.id = :empId and a.attendanceDate = :date ")
   boolean duplicateCheck(UUID empId, LocalDate date);

    @Query("select a from AttendanceUpdate  a where  a.updateStatus is null  and" +
            " (:month is not null and month(a.attendanceDate)=:month ) or" +
            " (:year is not null and  year (a.attendanceDate)=:year) or" +
            "(:status is not null and  a.updateStatus=:status) ")
    Optional<List<AttendanceUpdate>> fetchForAdminView(String status, int month, int year);

}
