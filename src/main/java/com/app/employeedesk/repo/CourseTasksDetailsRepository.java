package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CourseTasksDetailsOutputDto;
import com.app.employeedesk.dto.EmployeeCourseTaskDetailsViewDto;
import com.app.employeedesk.entity.CourseTasksDetails;
import com.app.employeedesk.entity.EmployeeCourseTaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseTasksDetailsRepository extends JpaRepository<CourseTasksDetails, UUID> {

    @Query("select new com.app.employeedesk.dto.EmployeeCourseTaskDetailsViewDto(c.id,c.name,et.status,c.taskImage) " +
            "from CourseTasksDetails c left join EmployeeCourseTaskDetails " +
            "et on et.courseTasksDetails.id=c.id where c.courseSubTopicDetails.id =:subTopicId")
    List<EmployeeCourseTaskDetailsViewDto> findEmployeeAllTaskDetails(UUID subTopicId);





}
