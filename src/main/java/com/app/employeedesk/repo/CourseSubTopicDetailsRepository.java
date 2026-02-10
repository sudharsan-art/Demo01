package com.app.employeedesk.repo;

import com.app.employeedesk.entity.CourseSubTopicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourseSubTopicDetailsRepository extends JpaRepository<CourseSubTopicDetails, UUID> {

}
