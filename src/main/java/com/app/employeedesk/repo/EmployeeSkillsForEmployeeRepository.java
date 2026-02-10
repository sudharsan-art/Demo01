package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeSkillsForEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface EmployeeSkillsForEmployeeRepository extends JpaRepository<EmployeeSkillsForEmployee, UUID>, JpaSpecificationExecutor<EmployeeSkillsForEmployee> {
   EmployeeSkillsForEmployee findByEmployeeBasicDetails(EmployeeBasicDetails employeeBasicDetails);

}
