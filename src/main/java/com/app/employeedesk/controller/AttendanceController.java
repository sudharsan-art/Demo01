package com.app.employeedesk.controller;


import com.app.employeedesk.dto.AttendanceUpdateAdminFilter;
import com.app.employeedesk.dto.AttendanceUpdateDto;
import com.app.employeedesk.dto.EmployeeAttendanceUpdateDto;
import com.app.employeedesk.dto.UpdateAttendanceDto;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.AttendanceService;
import com.app.employeedesk.service.EmployeePersonalDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance/")
public class AttendanceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResponseGenerator responseGenerator;

    private final AttendanceService attendanceService;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    @PostMapping(value = "login/{workMode}")
    public ResponseEntity<Response> checkin(@PathVariable String workMode, @RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("login an employee daily attendance {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, attendanceService.checkIn(workMode, principal), HttpStatus.OK);

        } catch (Exception e) {
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "logout")
    public ResponseEntity<Response> checkout(@RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("logout an employee daily attendance {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, attendanceService.checkOut(principal), HttpStatus.OK);

        } catch (Exception e) {
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = "update")
    public ResponseEntity<Response> update(@RequestHeader HttpHeaders httpHeader, @RequestBody UpdateAttendanceDto dto, Principal principal) {
        logger.info("update an employee daily attendance {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, attendanceService.updateAttendance(dto), HttpStatus.OK);

        } catch (Exception e) {
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "search/{empId}")
    public ResponseEntity<Response> search(@RequestHeader HttpHeaders httpHeader, @PathVariable String empId, Principal principal) {
        logger.info("update an employee daily attendance {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, attendanceService.searchEmployee(empId), HttpStatus.OK);

        } catch (Exception e) {
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = "updateRequest")
    public ResponseEntity<Response> updateRequest(@RequestHeader HttpHeaders httpHeaders,Principal principal, @RequestBody EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto){
        logger.info("registering for attendance update ", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,attendanceService.employeeAttendanceUpdate(employeeAttendanceUpdateDto,principal),HttpStatus.OK);
        }catch (CustomValidationsException | ParseException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);

        }

    }
    @GetMapping(value = "getUpdateList")
    public ResponseEntity<Response> getUpdateList(@RequestHeader HttpHeaders httpHeaders, Principal principal, @RequestBody AttendanceUpdateAdminFilter attendanceUpdateAdminFilter){
        logger.info("giving the admin view" , LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,attendanceService.getRecordsAdminAproval(attendanceUpdateAdminFilter),HttpStatus.OK);
        } catch (Exception e) {
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = "adminUpdate")
    public ResponseEntity<Response> adminUpdateAttendance(@RequestHeader HttpHeaders httpHeaders,@RequestBody EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto){
        logger.info("admin status check and give permission", LocalDate.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,attendanceService.adminResponseStatus(employeeAttendanceUpdateDto),HttpStatus.OK);
        }catch (CustomValidationsException | ParseException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);

        }
    }
    @GetMapping(value="getIntimeOutTime")
    public ResponseEntity<Response> getInTimeOutTime(@RequestHeader HttpHeaders httpHeaders,Principal principal,@RequestParam("date") String date){
        logger.info("get the IntimeOutTime for date", LocalDate.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,attendanceService.getInTimeOutTime(date,principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping(value="getUserNameByPrincipal")
    public ResponseEntity<Response> getUserNameByPrincipal(Principal principal,@RequestHeader HttpHeaders httpHeaders){
        logger.info("get Employee name by principal", LocalDate.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeePersonalDetailsService.getEmployeeNameByPricipal(principal
            ),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping(value = "rejectUpdate")
    public ResponseEntity<Response> rejectUpdate(@RequestHeader HttpHeaders httpHeaders, @RequestBody EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto){
        logger.info("update the admin has rejected the request",LocalDate.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,attendanceService.rejectProcess(employeeAttendanceUpdateDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping(value = "employeeScreenFetch")
    public ResponseEntity<Response> employeeScreenFetch(@RequestHeader HttpHeaders httpHeaders){
        logger.info("fetching the employee details based on the filter",LocalDate.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,attendanceService,HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "employeeAttendanceView")
    public ResponseEntity<Response> employeeAttendanceView(@RequestHeader HttpHeaders httpHeaders,@RequestBody AttendanceUpdateAdminFilter attendanceUpdateAdminFilter,Principal principal){
        logger.info("fetching the date for the employee attendance request history",LocalDate.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,attendanceService.employeeScreenFilter(attendanceUpdateAdminFilter,principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }


    }








}
