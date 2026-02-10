package com.app.employeedesk.controller;


import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.StateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/State")
@RequiredArgsConstructor
public class StateController {
    private final StateService stateService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;

    @GetMapping("/viewAll")
    public ResponseEntity<Response> viewall(@RequestHeader HttpHeaders httpHeader){
        logger.info("view all the states {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,stateService.viewall(), HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while view all the states  ",e);
           return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewByName/{stateName}")
    public ResponseEntity<Response> viewByName(@PathVariable String stateName,@RequestHeader HttpHeaders httpHeader){
        logger.info("view by state name {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,stateService.viewByName(stateName),HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while view by state name ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/Create")
    public ResponseEntity<Response> create(@RequestBody StateDto stateObject, @RequestHeader HttpHeaders httpHeader){
        logger.info("create new state {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,stateService.create(stateObject),HttpStatus.OK);
        }
        catch (CustomValidationsException e){
            logger.error("error occurred while creating the state ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);

        }
        catch (ListOfValidationException g){
            logger.error("error occurred while creating the state ",g);
            return responseGenerator.errorResponses(context,g.getErrorMessages(),HttpStatus.BAD_REQUEST);

        }
    }
    @GetMapping("/getState")
    public ResponseEntity<Response> getState(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeader){
        logger.info("get the state for the update {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,stateService.getState(id),HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while get the state for the update ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Response> update(@RequestBody StateDto stateDto ,@RequestHeader HttpHeaders httpHeader){
        logger.info("updating the state {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,stateService.update(stateDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while updating the state ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            logger.error("error occurred while updating the state ",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<Response> delete(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeader){
        logger.info("delete the state {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,stateService.delete(id),HttpStatus.OK);
        }
        catch (Exception e){
            logger.error("error occurred while delete the state ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
