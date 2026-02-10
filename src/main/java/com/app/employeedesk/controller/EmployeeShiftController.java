package com.app.employeedesk.controller;

import com.app.employeedesk.dto.EmployeeShiftDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeShiftWeekOffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/shift")
@RequiredArgsConstructor
public class EmployeeShiftController {
    private final EmployeeShiftWeekOffService employeeShiftWeekOffService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;

    @PostMapping("/create")
    public ResponseEntity<Response> createShift(@RequestBody EmployeeShiftDto employeeShiftDto,@RequestHeader HttpHeaders httpHeaders){
        logger.info("create the shift  {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeeShiftWeekOffService.shiftCreate(employeeShiftDto), HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/getShift")
    public ResponseEntity<Response> getShift(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeaders){
        logger.info("get the shift based on the id {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeeShiftWeekOffService.getEmployeeShiftById(id), HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteShift")
    public ResponseEntity<Response> deleteShift(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeaders){
        logger.info("delete the shift based on the id {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeeShiftWeekOffService.deleteShift(id), HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }



}
