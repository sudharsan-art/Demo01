package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeCourseSubTopicDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeCourseSubTopicDetailsRepository extends JpaRepository<EmployeeCourseSubTopicDetails, UUID> {
}
