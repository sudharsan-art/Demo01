package com.app.employeedesk.controller;

import com.app.employeedesk.dto.HolidayDto;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.HolidayMasterService;
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
@RequestMapping(value = "/holiday/")
public class HolidayController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResponseGenerator responseGenerator;

    private final HolidayMasterService holidayMasterService;

    @PostMapping("create")
    public ResponseEntity<Response> createNewHoliday(@RequestBody HolidayDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("create new holiday for this year{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, holidayMasterService.createNewHoliday(dto), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while create new holiday  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Response> deleteHoliday(@PathVariable String id, @RequestHeader HttpHeaders httpHeader) {
        logger.info("delete particular holiday for this year{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, holidayMasterService.deleteHoliday(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while delete a holiday  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("update")
    public ResponseEntity<Response> update(@RequestBody HolidayDto dto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("update holiday for this year{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, holidayMasterService.updateHoliday(dto), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while update the holiday  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("get/all")
    public ResponseEntity<Response> getAllHoliday( @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all holiday for this year{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, holidayMasterService.getAllHoliday(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while get all holiday  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
