package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeCourseTopicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface EmployeeCourseTopicDetailsRepository extends JpaRepository<EmployeeCourseTopicDetails, UUID> {

    @Query("select count(*) from EmployeeCourseTopicDetails e where e.employeeCourseEnrollmentDetails.id=:enrollmentId " +
            "and e.employeeCourseEnrollmentDetails.courseDetails.id=:courseId and e.status='COMPLETED' ")
    int getCompletedTopicCount(UUID enrollmentId,UUID courseId);

    @Query("select count(*) from EmployeeCourseTopicDetails e where e.employeeCourseEnrollmentDetails.id=:enrollmentId " +
            "and e.employeeCourseEnrollmentDetails.courseDetails.id=:courseId ")
    int getEmployeeTopicCount(UUID enrollmentId,UUID courseId);
}
