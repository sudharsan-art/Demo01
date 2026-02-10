package com.app.employeedesk.controller;

import com.app.employeedesk.dto.EmployeePayRollPaySlipDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeePayRollPaySlipService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/payslip")
@RequiredArgsConstructor
public class EmployeePayRollPaySlipController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    private final EmployeePayRollPaySlipService employeePayRollPaySlipService;

    @PostMapping("/createUpdate")
    @Qualifier
    public ResponseEntity<Response> createUpdatePaySlip(@RequestBody EmployeePayRollPaySlipDto employeePayRollPaySlipDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("create new employee payslip {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,employeePayRollPaySlipService.createUpdatePayslip(employeePayRollPaySlipDto) , HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while payslip details are empty ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getPayslipDetails")
    public ResponseEntity<Response> getEmployeePaySlipDetails(@RequestBody UUID employeeId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get employee payslip details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,employeePayRollPaySlipService.getEmployeePaySlipDetails(employeeId) , HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while payslip details are wrong ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
