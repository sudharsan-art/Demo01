package com.app.employeedesk.controller;


import com.app.employeedesk.dto.CourseDetailsInputDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CourseTopicDetailsService;
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
@RequestMapping("/topic")
@RequiredArgsConstructor
public class CourseTopicDetailsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    private final CourseTopicDetailsService courseTopicDetailsService;


    @GetMapping("/getCourseAllDetails")
    public ResponseEntity<Response> getAllWeekTopicSubtopic(@RequestBody UUID courseId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get course all details  {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTopicDetailsService.getCourseAllDetails(courseId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while invalid course id ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/addUpdateDeleteTopicAndSubTopicDetails")
    public ResponseEntity<Response> addUpdateDeleteTopicAndSubTopicDetails(@RequestBody CourseDetailsInputDto courseDetailsInputDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("add update delete topic And subTopic details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTopicDetailsService.addUpdateDeleteCourseTopicSubTopicDetails(courseDetailsInputDto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while add update delete topic And subTopic details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
