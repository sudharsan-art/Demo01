package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeWorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface EmployeeExperienceRepository extends JpaRepository<EmployeeWorkExperience, UUID> {
    List<EmployeeWorkExperience> findByEmployeeBasicDetails(EmployeeBasicDetails employeeBasicDetails);
}
