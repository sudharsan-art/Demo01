package com.app.employeedesk.controller;

import com.app.employeedesk.dto.ComboOffResponseDto;
import com.app.employeedesk.dto.HolidayPresentComboDto;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.ComboOffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("comboOff")
@RequiredArgsConstructor
public class ComboOffController {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;
    private final ComboOffService comboOffService;

    @GetMapping("/GetCombo")
    public ResponseEntity<?> getComboInfo(@RequestHeader HttpHeaders httpHeaders, Principal principal, @RequestParam String date){
        logger.info("Get the Holiday name {}", LocalDate.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,comboOffService.getComboInfoBasedDate(principal,date), HttpStatus.OK);
        }catch (Exception e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/CreateCombReq")
    public ResponseEntity<?> CreateComboRequest(@RequestHeader HttpHeaders httpHeaders, Principal principal, @RequestBody HolidayPresentComboDto dto){
        logger.info("create the combo off request {}",LocalDate.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,comboOffService.saveEmployeeRequest(dto,principal),HttpStatus.OK);
        }catch (Exception e){
            return responseGenerator.errorResponses(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
