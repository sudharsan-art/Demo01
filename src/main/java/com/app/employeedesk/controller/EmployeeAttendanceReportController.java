package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AdminFilterRequestDTO;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.AdminFilterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/attendanceReport")
@RequiredArgsConstructor
public class EmployeeAttendanceReportController {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;
    private final AdminFilterService adminFilterService;




    @GetMapping("/filterByInput")
    public ResponseEntity<Response> filterByInput(@RequestBody AdminFilterRequestDTO adminFilterRequestDTO, @RequestHeader HttpHeaders httpHeader){
        logger.info("generate monthly employee payslip {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,adminFilterService.adminFilterResponse(adminFilterRequestDTO),HttpStatus.OK);
        }
        catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

    }


}
