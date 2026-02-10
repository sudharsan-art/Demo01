package com.app.employeedesk.controller;

import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.EmployeeMonthlyPayslipService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/employeeMonthlyPayslip")
@RequiredArgsConstructor
public class EmployeeMonthlyPayslipController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    private final EmployeeMonthlyPayslipService employeeMonthlyPayslipService;

//    @PostMapping("/generate")
//    public ResponseEntity<Response> generateEmployeeMonthlyPayslip(@RequestHeader HttpHeaders httpHeader) {
//        logger.info("generate monthly employee payslip {}", LocalDateTime.now());
//        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//        try {
//            return responseGenerator.successResponse(context,employeeMonthlyPayslipService.generateEmployeePayslip() , HttpStatus.OK);
//        } catch (CustomValidationsException e) {
//            logger.error("error occurred while payslip details are empty ", e);
//            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (JSONException e) {
//            logger.error("error occurred while payslip details into json ", e);
//            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
    @GetMapping("/generatePdf")
    public ResponseEntity<Object> generatePdf(@RequestParam("employeeId") UUID employeeId, @RequestParam("date") String date) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("payslip.pdf").build());
            return new ResponseEntity<>(employeeMonthlyPayslipService.employeePayslipPdf(employeeId,date),headers,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Automatically download when we hit api
//headers.setContentDisposition(ContentDisposition.builder("attachment").filename("payslip.pdf").build());








//@GetMapping("/getEmployeePayslipDetails")
//public ResponseEntity<Response> generateEmployeeMonthlyPayslip(@RequestBody EmployeeMonthlyPayslipInputDetailsDto employeeMonthlyPayslipInputDetailsDto, @RequestHeader HttpHeaders httpHeader) {
//    logger.info("get monthly employee payslip details {}", LocalDateTime.now());
//    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//    try {
//        return responseGenerator.successResponse(context,employeeMonthlyPayslipService.getEmployeeMonthlyPayslipDetails(employeeMonthlyPayslipInputDetailsDto) , HttpStatus.OK);
//    } catch (CustomValidationsException e) {
//        logger.error("error occurred while payslip details are empty ", e);
//        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//    } catch (IOException e) {
//        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//}


}
