package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeShiftEntity;
import com.app.employeedesk.entity.EmployeeWeekOff;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface EmployeeShitRepository extends JpaRepository<EmployeeShiftEntity, UUID> {
    boolean existsByShiftName(String shiftName);

//    @Query(value = "select case when count (*)>=1 then true else false end " +
//            "from EmployeeBasicDetails e inner join EmployeeShiftEntity es on es.id=e.employeeShiftEntity.id " +
//            "left join EmployeeWeekOff ew on ew.shiftid=es.id " +
//            "where e.id=:empId and es.")
//    boolean checkSiftTiming(LocalTime fromTime, LocalTime endTime, UUID empId);

    @Query("select emp.employeeShiftEntity.employeeWeekOff  from EmployeeBasicDetails emp where emp.id=:empId ")
    List<EmployeeWeekOff> findByEmployeeWeekOff(UUID empId);
}
