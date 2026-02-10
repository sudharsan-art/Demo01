package com.app.employeedesk.controller;


import com.app.employeedesk.dto.AttendanceFilterDto;
import com.app.employeedesk.dto.AttendanceFilterSearchDTO;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.PrimitiveIterator;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    @GetMapping("/generate-excel-report")
    public ResponseEntity<byte[]> employeeAttendanceReport(Principal principal, @RequestBody AttendanceFilterSearchDTO attendanceFilterSearchDTO, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get employee attendance report details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponses(context, reportService.generateExcelReport(attendanceFilterSearchDTO,principal), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error generating report", e);
            return  responseGenerator.errorResponses(context,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees-monthly-salary")
    public ResponseEntity<byte[]> generateEmployeeMonthlyReport(@RequestBody AttendanceFilterDto attendanceFilterDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get employee attendance report details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponses(context, reportService.generateEmployeeMonthlyReport(attendanceFilterDto), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error generating report", e);
            return  responseGenerator.errorResponses(context,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
