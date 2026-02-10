package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeCourseTaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeCourseTaskDetailsRepository extends JpaRepository<EmployeeCourseTaskDetails, UUID> {

    @Query("select ct from EmployeeCourseTaskDetails ct where ct.courseTasksDetails.id=:taskId " +
            "and ct.employeeCourseSubTopicDetails.id=:employeeSubTopicId")
    Optional<EmployeeCourseTaskDetails> getEmployeeTaskDetails(UUID taskId, UUID employeeSubTopicId);
}
