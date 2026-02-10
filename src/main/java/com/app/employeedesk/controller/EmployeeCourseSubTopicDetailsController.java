package com.app.employeedesk.controller;

import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeCourseSubTopicDetailsService;
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
@RequestMapping("/employeeCourseSubTopic")
@RequiredArgsConstructor
public class EmployeeCourseSubTopicDetailsController {

    private final EmployeeCourseSubTopicDetailsService employeeCourseSubTopicDetailsService;
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    @GetMapping("/getEmployeeMaterialAndReferenceLink")
    public ResponseEntity<Response> getEmployeeMaterialAndReferenceLink(@RequestBody String subTopicId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all employee material and reference link details{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,employeeCourseSubTopicDetailsService.getEmployeeMaterialAndReferenceLink(subTopicId), HttpStatus.OK);
        }
        catch (CustomValidationsException e){
            logger.error("error occurred while get all employee material and reference link details ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
