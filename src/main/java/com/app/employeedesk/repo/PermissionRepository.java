package com.app.employeedesk.repo;

import com.app.employeedesk.bean.WeekOfBean;
import com.app.employeedesk.dto.PermissionListDto;
import com.app.employeedesk.entity.LeaveRequest;
import com.app.employeedesk.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    @Query(value = "select l " +
            "from  LeaveRequest l " +
            "where l.employeeId.id = :empId and (:date between l.fromDate and l.endDate) and l.leaveStatus='FULL_DAY'")
    Optional<LeaveRequest> findByAlreadyExistDate(@Param("date") LocalDate date, @Param("empId") UUID empId);

    @Query(value = "select p from Permission p where p.date=:date and p.employeeId.id=:id")
    Optional<Permission> dateAlreadyExist(UUID id, LocalDate date);

    @Query("select p from Permission p where p.id !=:permissionId and p.date=:date")
    Optional<Permission> checkPermissionDateAlreadyExistWhitOutSameId(UUID permissionId, LocalDate date);

    @Query("select p from Permission p where p.employeeId.id=:id")
    Optional<List<Permission>> findAllPermission(UUID id);

    @Query(value = "SELECT new com.app.employeedesk.dto.PermissionListDto(p.id, e.employeecode, " +
            "concat(e.firstName,' ',e.middleName,' ',e.lastName),p.startTime,p.endTime, p.date) " +
            "FROM Permission p " +
            " inner join EmployeeBasicDetails e ON e.id = p.employeeId.id " +
            "WHERE p.status = 'PENDING'")
    Optional<List<PermissionListDto>> getAllPendingPermissions();

    @Query(value = "select w.week_days as days,w.month_days_position as weekNo from employee_week_off w where w.shift_id=:siftId", nativeQuery = true)
    List<WeekOfBean> findId(String siftId);

    @Query(value = "select p from Permission p where p.employeeId.id=:empId and p.date=:today and p.status='ACCEPT'")
    Optional<Permission> findByTheyTakePermission(UUID empId, LocalDate today);
}
