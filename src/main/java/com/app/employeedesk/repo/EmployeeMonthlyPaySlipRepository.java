package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeeMonthlyPayslipOutputDto;
import com.app.employeedesk.entity.EmployeeMonthlyPayslip;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeMonthlyPaySlipRepository extends JpaRepository<EmployeeMonthlyPayslip, UUID> {


    @Query("select new com.app.employeedesk.dto.EmployeeMonthlyPayslipOutputDto(e.id,e.firstName,e.employeecode,e.department,e.desigination,em.payslipData,em.generatedOn) " +
            "from EmployeeMonthlyPayslip em inner join EmployeeBasicDetails e on e.id=em.employeeBasicDetails.id " +
            "where  extract(month from em.generatedOn)=:month " +
            "and extract(year from em.generatedOn) =:year " +
            "and em.employeeBasicDetails.id=:employeeId")
    Optional<EmployeeMonthlyPayslipOutputDto> getEmployeeMonthlyPayslipDetails(@Param("employeeId") UUID employeeId,
                                                                               @Param("month") String month,
                                                                               @Param("year") String year);

    @Query("select emp.payslipData from EmployeeMonthlyPayslip emp where emp.employeeBasicDetails.id=:employeeId")
    String getEmployeePaySlipJson(@Param("employeeId") UUID employeeId);

    @Query("select max(d.generatedOn) from EmployeeMonthlyPayslip d where d.employeeBasicDetails.id=:empId ")
    LocalDate employeeLastGeneratedPayslip(UUID empId);
}
