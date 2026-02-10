package com.app.employeedesk.controller;

import com.app.employeedesk.entity.EmployeeCourseTaskDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeCourseSubTopicDetailsService;
import com.app.employeedesk.service.EmployeeCourseTaskDetailsService;
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
@RequestMapping("/EmployeeCourseTaskDetails")
public class EmployeeCourseTaskDetailsController {

    private final EmployeeCourseTaskDetailsService employeeCourseTaskDetailsService;
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;





}
