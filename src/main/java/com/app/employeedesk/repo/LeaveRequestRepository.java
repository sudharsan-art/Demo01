package com.app.employeedesk.repo;

import com.app.employeedesk.dto.LeaveRequestListDto;
import com.app.employeedesk.entity.LeaveRequest;
import com.app.employeedesk.enumeration.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {
    @Query("select l from LeaveRequest l where l.employeeId.id=:employeeId")
    Optional<List<LeaveRequest>> findByEmployeeId(UUID employeeId);

    @Query(value = " select new com.app.employeedesk.dto.LeaveRequestListDto(l.id,emb.employeecode, " +
            "concat(emb.firstName,' ',emb.middleName,' ',emb.lastName),l.fromDate,l.endDate,l.leaveType) " +
            " from LeaveRequest  l " +
            "inner join EmployeeBasicDetails emb on emb.id=l.employeeId.id where l.leaveStatus='PENDING'")
    Optional<List<LeaveRequestListDto>> getAllPendingRequest();

    @Query("SELECT CASE WHEN " +
            "count(*) >= 1  " +
            "THEN true ELSE false END " +
            "FROM LeaveRequest l " +
            "WHERE l.employeeId.id = :empId AND l.leaveStatus IN (:statuses) and " +
            "((l.fromDate BETWEEN :fromDate AND :endDate) or (l.endDate BETWEEN :fromDate AND :endDate))")
  Optional<Boolean>isDateBetweenFromAndEndDate(@Param("fromDate") LocalDate fromDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("empId") UUID empId,
                                        @Param("statuses") List<LeaveStatus> statuses);


    @Query("SELECT " +
            "CASE WHEN " +
            "count(*) >= 1  " +
            "THEN true ELSE false END " +
            "FROM LeaveRequest l " +
            "WHERE (l.employeeId.id = :empId AND l.leaveStatus IN (:statuses)) " +
            "and l.id != :specificLeaveId and " +
            "((l.fromDate BETWEEN :fromDate AND :endDate) or (l.endDate BETWEEN :fromDate AND :endDate))")
   Optional <Boolean> isDateBetweenFromAndEndDateOrSpecificLeave(@Param("fromDate") LocalDate fromDate,
                                                       @Param("endDate") LocalDate endDate,
                                                       @Param("empId") UUID empId,
                                                       @Param("statuses") List<LeaveStatus> statuses,
                                                       @Param("specificLeaveId") UUID specificLeaveId);

    @Query(value = "select case when count(*) >=1 then true else false end " +
            "from LeaveRequest l inner join Attendance a on a.employeeId=l.employeeId.id " +
            "where l.employeeId.id=:id and " +
            "((l.fromDate BETWEEN :fromDate AND :endDate) or (l.endDate BETWEEN :fromDate AND :endDate)) " +
            "and a.status='PRESENT'")
    Optional<Boolean> approvalValidation(@Param("id") UUID id, @Param("fromDate") LocalDate givenDate, @Param("endDate") LocalDate givenDate2);

@Query(value = "select l from LeaveRequest l where l.leaveStatus=:status and l.fromDate=:today")
   Optional <List<LeaveRequest>> todayPendingRequest(LeaveStatus status, LocalDate today);
@Query(value = "select case when count(*) >=1 then true else false end " +
        "from LeaveRequest l where (:date between l.fromDate and l.endDate) and l.leaveStatus='ACCEPT'")
    Optional<Boolean> checkDate(LocalDate date);
}
