package com.app.employeedesk.repo;


import com.app.employeedesk.entity.CourseTopicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourseTopicDetailsRepository extends JpaRepository<CourseTopicDetails, UUID> {

}
