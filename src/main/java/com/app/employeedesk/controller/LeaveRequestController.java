package com.app.employeedesk.controller;

import com.app.employeedesk.dto.LeaveFilterDto;
import com.app.employeedesk.dto.LeavePermissionDto;
import com.app.employeedesk.dto.LeaveRequestDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.LeaveRequestService;
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
@RequiredArgsConstructor
@RequestMapping("/Leave/request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    private final ResponseGenerator responseGenerator;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/create")
    public ResponseEntity<Response> createLeave(@RequestBody LeaveRequestDto dto, @RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("create a new leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.createLeaveRequest(principal, dto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while creating new leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateLeave(@RequestBody LeaveRequestDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("update a leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.updateLeaveRequest(dto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while update a leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<Response> getAll(@RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("get all  leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.getAllLeaveRequest(principal), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while get all leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable String id, @RequestHeader HttpHeaders httpHeader) {
        logger.info("delete leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.delete(id), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while deleting a leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterLeaveRequest(@RequestBody LeaveFilterDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("filter all employee leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.requestFilter(dto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while getting all employee leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approvel")
    public ResponseEntity<Response> approveLeaveRequest(@RequestBody LeavePermissionDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("approve a leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.approveLeaveRequest(dto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while approve a leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all/employee/request")
    public ResponseEntity<Response> filterLeaveRequest(@RequestHeader HttpHeaders httpHeader) {
        logger.info("get all employee leave request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, leaveRequestService.getAllPendingEmployee(), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while getting all employee leave request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);


        }
    }
}
