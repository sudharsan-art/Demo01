package com.app.employeedesk.controller;

import com.app.employeedesk.dto.ShiftMasterV3Dto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.ShiftMasterV3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shift/v3")
public class ShiftMasterV3Controller {

    private final ShiftMasterV3Service shiftService;
    private final ResponseGenerator responseGenerator;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/create")
    public ResponseEntity<Response> createShift(@RequestBody ShiftMasterV3Dto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("create a new shift master v3 request at {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, shiftService.createShift(dto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while creating new shift master v3", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("unexpected error occurred while creating new shift master v3", e);
            return responseGenerator.errorResponse(context, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<Response> getAll(@RequestHeader HttpHeaders httpHeader) {
        logger.info("get all shift master v3 records at {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, shiftService.getAllShifts(), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while fetching all shifts v3", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("unexpected error occurred while fetching shifts v3", e);
            return responseGenerator.errorResponse(context, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable UUID id, @RequestHeader HttpHeaders httpHeader) {
        logger.info("delete shift master v3 with id {} at {}", id, LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, shiftService.deleteShift(id), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while deleting shift v3", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("unexpected error occurred while deleting shift v3", e);
            return responseGenerator.errorResponse(context, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
