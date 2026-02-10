package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CourseDetailsViewDto;
import com.app.employeedesk.dto.CourseEnrollmentCompletedDto;
import com.app.employeedesk.dto.CourseEnrollmentProgressDto;
import com.app.employeedesk.entity.EmployeeCourseEnrollmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeCourseEnrollmentRepository extends JpaRepository<EmployeeCourseEnrollmentDetails, UUID> {

    @Query("select new com.app.employeedesk.dto.CourseEnrollmentProgressDto(en.employeeBasicDetails.id,en.employeeBasicDetails.employeecode,en.employeeBasicDetails.firstName,en.id,en.startDate) " +
            "from EmployeeCourseEnrollmentDetails en " +
            "where en.courseDetails.id = :courseId " +
            "and en.courseStatus='PROGRESS'")
    List<CourseEnrollmentProgressDto> getAllCourseEnrollmentProgress(UUID courseId);

    @Query("select new com.app.employeedesk.dto.CourseEnrollmentCompletedDto(en.employeeBasicDetails.employeecode,en.employeeBasicDetails.firstName,en.startDate,en.endDate,en.remarks) " +
            "from EmployeeCourseEnrollmentDetails en " +
            "where en.courseDetails.id = :courseId " +
            "and en.courseStatus ='COMPLETED'")
    List<CourseEnrollmentCompletedDto> getAllCourseEnrollmentComplete(UUID courseId);



    @Query("select new com.app.employeedesk.dto.CourseDetailsViewDto(c.id, c.courseName, en.id, en.startDate, en.endDate) " +
            "from EmployeeCourseEnrollmentDetails en " +
            "inner join  en.courseDetails c " +
            "where en.employeeBasicDetails.id = :employeeId")
    List<CourseDetailsViewDto> getEmployeeAllEnrolledCourseDetails(UUID employeeId);










//    @Query("select new com.app.employeedesk.dto.EmployeeEnrolledCourseCompletedViewDto(cw.courseWeeksDetails.weekName,collect(ct.courseTopicDetails.topicName))" +
//            "from EmployeeCourseEnrollmentDetails en " +
//            "inner join EmployeeCourseWeekDetails cw on en.id=cw.employeeCourseEnrollmentDetails.id " +
//            "inner join EmployeeCourseTopicDetails ct on ct.employeeCourseWeekDetails.id=cw.id " +
//            "where en.id=:enrollmentId and cw.status='COMPLETED' group by cw.courseWeeksDetails.weekName")
//    List<EmployeeEnrolledCourseCompletedViewDto> getAllEmployeeEnrolledCourseCompletedList(UUID enrollmentId);
}
