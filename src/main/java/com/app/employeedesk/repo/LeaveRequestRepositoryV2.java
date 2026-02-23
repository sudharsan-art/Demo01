package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LeaveRequestV2;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LeaveRequestRepositoryV2 extends JpaRepository<LeaveRequestV2, UUID> {
    List<LeaveRequestV2> findByEmployee_Id(UUID employeeId);

    List<LeaveRequestV2> findByStatus(LeaveStatus status);

    @Query("""
        select coalesce(sum(l.numberOfDays), 0)
        from LeaveRequestV2 l
        where l.employee.id = :employeeId
        and l.leaveType = :leaveType
        and l.status = com.app.employeedesk.enumeration.LeaveStatus.ACCEPT
        and l.fromDate between :startDate and :endDate
        """)
    int sumApprovedLeavesForEmployeeThisYear(
            @Param("employeeId") UUID employeeId,
            @Param("leaveType") LeaveType leaveType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    boolean existsByEmployee_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            UUID employeeId,
            LocalDate toDate,
            LocalDate fromDate
    );


}
