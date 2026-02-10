package com.app.employeedesk.controller;

import com.app.employeedesk.dto.CourseDetailsDto;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CourseDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseDetailsController {

    private final CourseDetailsService courseDetailsService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;


    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteCourse(@RequestBody UUID courseId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("delete course details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseDetailsService.deleteCourse(courseId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while delete course details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllCourseDetails(@RequestHeader HttpHeaders httpHeader) {
        logger.info("get all course details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseDetailsService.findAllCourseDetails(), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all course details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
