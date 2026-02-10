package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EducationDetails;
import com.app.employeedesk.entity.EducationDetailsEmbedable;
import com.app.employeedesk.entity.EducationQualification;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EducationDetailsRepository extends JpaRepository<EducationDetails, EducationDetailsEmbedable> {

    @Query("select e from EducationDetails  e where e.id.employeeBasicDetails=:empId")
    List<EducationDetails> findByemployeeId(EmployeeBasicDetails empId);
    Optional<EducationDetails> findById(EducationDetailsEmbedable educationId);
}
