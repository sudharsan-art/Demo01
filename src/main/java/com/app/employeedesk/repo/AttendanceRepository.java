package com.app.employeedesk.repo;

import com.app.employeedesk.bean.ComboOffIntimeOutTime;
import com.app.employeedesk.bean.EmployeeAttendance;
import com.app.employeedesk.dto.AttendanceFilterResponseDTO;
import com.app.employeedesk.dto.EmployeeAttendanceDto;
import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.entity.AttendanceTiming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    @Query(value = "select a from Attendance a where a.date=:date and a.employeeId=:empId")
    Optional<Attendance> findByDateAndEmpId(LocalDate date, UUID empId);

    List<Attendance> findByDate(LocalDate today);

    Attendance findByEmployeeId(UUID empId);

    Attendance findByEmployeeIdAndDate(UUID empId, LocalTime localTime);

    @Query("select case when count(a) =0 then false when count (a)>0 then true end from Attendance a where a.date=:date and a.employeeId=:employee and  a.workMode='HOLIDAY'")
    Boolean checkEmployeeAbsentOrHoliday(LocalDate date, UUID employee);

    @Query("select at from Attendance a inner join  AttendanceTiming  at on at.attendances=a where a.date=:date and a.employeeId=:empId and  (a.status='ABSENT' or a.status='HALF_DAY')")
    List<AttendanceTiming> getAttendanceTiming(LocalDate date,UUID empId);
    @Query("select a from Attendance a where a.date=:date and a.employeeId=:empId and a.workMode='ABSENT'")
    Attendance getAttendance(LocalDate date,UUID empId);

    @Query("select  a as attendance,at as attendanceTiming from Attendance a inner join AttendanceTiming at on at.attendances=a where a.employeeId=:empId and a.date=:date ")
    List<EmployeeAttendance> getAttendanceTimingByAttendance(UUID empId,LocalDate date);




    @Query("select max(at.outTime) as outTime,min(at.inTime) as inTime from Attendance a inner join AttendanceTiming at on a=at.attendances where a.employeeId=:empId and a.date=:dates and a.status='HOLIDAY_BUT_PRESENT'")
    ComboOffIntimeOutTime getTimeForComboOff(UUID empId, LocalDate dates);






//    @Query(value = "select a.employeeId from Attendance  a where a.date=:date and a.status='PRESENT'")
//   Optional <List<UUID>> findByEmpIdGivenDate(LocalDate date);
//
//    @Query(value = "select a from Attendance a where a.date=:date and a.employeeId=:empId")
//    Attendance findByIdWithDate(UUID empId, LocalDate date);
//
//    @Query(value = "select a from  Attendance  a where a.checkOut is null and a.date=:today")
//    Optional<List<Attendance>> findByDontLogoutEmployee(LocalDate today);
//
//    @Query(value = "select a from Attendance a where a.checkOut is null and a.date=:date and a.employeeId=:employeeId")
//    Attendance findByEmployee(LocalDate date, UUID employeeId);

























@Query("select new com.app.employeedesk.dto.AttendanceFilterResponseDTO(att.date,att.status,att.workMode,att.hours) from Attendance att  where att.employeeId=:empId and att.date between :startDate and :endDate")
          List<AttendanceFilterResponseDTO> attendanceReportFilter(LocalDate startDate, LocalDate endDate, UUID empId);

    @Query("select new com.app.employeedesk.dto.EmployeeAttendanceDto(a.employeeId, sum(a.hours)) " +
            "from Attendance a where YEAR(a.date) = :year and MONTH(a.date) = :month " +
            "group by a.employeeId")
    List<EmployeeAttendanceDto> getEmployeeAttendanceDetails(@Param("year") int year, @Param("month") int month);


// this is for learning the native query

//  @Query(nativeQuery = true ,value = "select * from attendance att where att.employee_id=:empId and att.date between :startDate and :endDate")
//  List<EmployeeAttendanceFilterBean> attendanceReportFilter(LocalDate startDate, LocalDate endDate, String empId, Pageable pageable);
//
//




}
