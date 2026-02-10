package com.app.employeedesk.repo;

import com.app.employeedesk.dto.FindEmployeeNameDto;
import com.app.employeedesk.dto.SiftAndWeekOfDetailsDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

public interface EmployeeBasicDetailsRepository extends JpaRepository< EmployeeBasicDetails, UUID> , JpaSpecificationExecutor<EmployeeBasicDetails> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);


    Optional<EmployeeBasicDetails> findByEmail(Principal principal);

    @Query(
            value = "select eb.id from UserDetails u " +
                    "inner join EmployeeBasicDetails eb " +
                    "on eb.id=u.employeeBasicDetails.id " +
                    "where u.status='ACTIVE'"
    )
    List<UUID> findByIdActiveEmployee();

    @Query(
            value = "select new com.app.employeedesk.dto.SiftAndWeekOfDetailsDto(emp.employeeShiftEntity.firstHalfStartTime,emp.employeeShiftEntity.firstHalfEndTime,emp.employeeShiftEntity.secoundHalfStartTime," +
                    "emp.employeeShiftEntity.secoundHalfEndTime,emp.employeeShiftEntity.shiftStartTime,emp.employeeShiftEntity.shiftEndTime,emp.employeeShiftEntity.id) " +
                    "from EmployeeBasicDetails emp  where emp.id=:empId")
    SiftAndWeekOfDetailsDto findByWeekAndSiftDetails(UUID empId);

    @Query(value = "select new com.app.employeedesk.dto.FindEmployeeNameDto(eb.id,concat(eb.firstName,' ',eb.lastName) ,eb.employeecode)from EmployeeBasicDetails  eb where eb.id=:employeeId")
    FindEmployeeNameDto findByIdEmployeeCode(UUID employeeId);
}

