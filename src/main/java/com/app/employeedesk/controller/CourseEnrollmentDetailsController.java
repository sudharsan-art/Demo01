package com.app.employeedesk.controller;

import com.app.employeedesk.dto.CourseEnrollmentDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeCourseEnrollmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courseEnrollment")
public class CourseEnrollmentDetailsController {

    private final EmployeeCourseEnrollmentService employeeCourseEnrollmentService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    @GetMapping("/getAllCompletedStatusDetails/{courseId}")
    public ResponseEntity<Response> getAllCompletedStatusDetails(@PathVariable String courseId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all course enrollment completed list {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.courseEnrollmentCompletedList(courseId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all course enrollment completed list ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllProgressStatusDetails/{courseId}")
    public ResponseEntity<Response> getAllProgressStatusDetails(@PathVariable String courseId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all course enrollment progress list {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.courseEnrollmentProgressList(courseId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all course enrollment progress list ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteCourseEnrollmentDetails(@RequestBody String courseEnrollmentId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("delete course enrollment details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.deleteEmployeeCourseEnrollment(courseEnrollmentId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while delete course enrollment details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/markAsCompleted")
    public ResponseEntity<Response> markAsCompleted(@RequestBody String courseEnrollmentId,@RequestBody String remarks, @RequestHeader HttpHeaders httpHeader) {
        logger.info("mark as complete the  course enrollment details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.markAsCompleted(courseEnrollmentId,remarks), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while update course enrollment details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<Response> employeeCourseEnrollment(@RequestBody CourseEnrollmentDto courseEnrollmentDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("employee course enrollment details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.employeeCourseEnrollment(courseEnrollmentDto),HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while save employee course enrollment details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmployeeAllEnrolledCourses/{employeeId}")
    public ResponseEntity<Response> getEmployeeAllEnrolledCourses(@PathVariable String employeeId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all employee enrolled courses list {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.getEmployeeAllEnrolledCourses(employeeId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all employee enrolled courses list ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getEmployeeCourseProgressDetails/{enrollmentId}")
    public ResponseEntity<Response> getEmployeeCourseProgressDetails(@PathVariable String enrollmentId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all employee enrolled courses progress list {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, employeeCourseEnrollmentService.getEmployeeCourseProgressDetails(enrollmentId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all employee enrolled courses progress list ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }







}
