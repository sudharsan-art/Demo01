package com.app.employeedesk.controller;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.PermissionService;
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
@RequestMapping("/Permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    private final ResponseGenerator responseGenerator;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/create")
    public ResponseEntity<Response> createLeave(@RequestBody PermissionDto dto, @RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("create a new permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.createPermission(dto, principal), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while creating new permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateLeave(@RequestBody PermissionDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("update a permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.updateUpdatePermission(dto), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while update a permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<Response> getAll(@RequestHeader HttpHeaders httpHeader, Principal principal) {
        logger.info("get all permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.getAllPermission(principal), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while get all permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterLeaveRequest(@RequestBody PermissionFilterDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("filter all employee permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.permissionFilter(dto), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while getting all employee permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approvel")
    public ResponseEntity<Response> approveLeaveRequest(@RequestBody LeavePermissionDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("approve a permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.approvePermission(dto), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while approve a permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all/employee/request")
    public ResponseEntity<Response> filterLeaveRequest(@RequestHeader HttpHeaders httpHeader) {
        logger.info("get all employee permission request{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, permissionService.getAllPendingPermission(), HttpStatus.OK);
        }
        catch (CustomValidationsException e) {
            logger.error("error occurred while getting all employee permission request", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
