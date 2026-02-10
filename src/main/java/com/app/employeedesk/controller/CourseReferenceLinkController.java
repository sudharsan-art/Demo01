package com.app.employeedesk.controller;

import com.app.employeedesk.dto.CourseReferenceLinkTopicDto;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CourseReferenceLinkService;
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
@RequestMapping("/referenceLink")
@RequiredArgsConstructor
public class CourseReferenceLinkController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    private final CourseReferenceLinkService courseReferenceLinkService;

    @PostMapping("/addUpdateDelete")
    public ResponseEntity<Response> addUpdateDeleteReferenceLink(@RequestBody CourseReferenceLinkTopicDto courseReferenceLinkTopicDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("add course reference link {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseReferenceLinkService.addUpdateDeleteReferenceLink(courseReferenceLinkTopicDto), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while save reference link ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getCourseAllDetailsWithReferenceLinks")
    public ResponseEntity<Response> getAllSubTopicReferenceLink(@RequestBody UUID courseId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all subtopic link {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseReferenceLinkService.getAllReferenceLinks(courseId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while get reference link ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
