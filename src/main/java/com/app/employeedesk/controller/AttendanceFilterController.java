package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AttendanceFilterSearchDTO;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeAttendanceReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/AttFilter")
@AllArgsConstructor
public class AttendanceFilterController {
    private final EmployeeAttendanceReportService employeeAttendanceReportService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;

    @GetMapping("/ReportFilter")
    public ResponseEntity<Response> attendanceDynamicFilter(HttpServletResponse response,Principal principal, @RequestBody AttendanceFilterSearchDTO attendanceFilterSearchDTO, @RequestHeader HttpHeaders httpHeader){
        logger.info("filtering the attendance based on the input field  {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,employeeAttendanceReportService.attendanceReportGenerator(response,attendanceFilterSearchDTO,principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
