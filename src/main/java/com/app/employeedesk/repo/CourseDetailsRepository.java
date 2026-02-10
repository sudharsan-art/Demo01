package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CourseDetailsDto;
import com.app.employeedesk.dto.CourseDetailsOutputDto;
import com.app.employeedesk.entity.CourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseDetailsRepository extends JpaRepository<CourseDetails, UUID> {
    boolean existsByCourseName(String courseName);
    CourseDetails findByCourseName(String name);

    @Query("select new com.app.employeedesk.dto.CourseDetailsDto(c.id,c.courseName,c.duration,c.description) " +
            "from CourseDetails c")
    Optional<List<CourseDetailsDto>> findAllCourseDetails();

    @Query("select new com.app.employeedesk.dto.CourseDetailsDto(c.id,c.courseName,c.duration,c.description) " +
            "from CourseDetails c where c.courseName like:courseName%")
    Optional<CourseDetailsDto> findCourseDetails(String courseName);




}
