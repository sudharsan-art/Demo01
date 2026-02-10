package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AdminRegisterDto;
import com.app.employeedesk.dto.EmployeeRegisterDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class UserRegisterController {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final UserRegisterService userRegisterService;

    private final ResponseGenerator responseGenerator;
    @PostMapping("/employee")
    public ResponseEntity<Response> employeeRegister(@RequestBody EmployeeRegisterDto request, @RequestHeader HttpHeaders httpHeader) {
        logger.info("New employee create {}", LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,userRegisterService.employeeRegister(request), HttpStatus.OK);
        }catch (ListOfValidationException e){
            logger.error("error occurred while register employee details ",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Response> adminRegister(@RequestBody AdminRegisterDto request, @RequestHeader HttpHeaders httpHeader) {
        logger.info("New admin create {}", LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,userRegisterService.adminRegister(request), HttpStatus.OK);
        }
        catch (Exception e){
            logger.error("error occurred while register admin details ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmployeeDetails")
    public ResponseEntity<Response> getEmployeeDetails(Principal principal, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get employee details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, userRegisterService.getEmployeeDetailsByPrinciple(principal), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get employee details ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
