package com.app.employeedesk.controller;

import com.app.employeedesk.dto.CityDto;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CityService;
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
@RequestMapping("/City")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;

    @GetMapping("/viewAll")
    public ResponseEntity<Response> viewall(@RequestHeader HttpHeaders httpHeader) {
        logger.info("view all the city {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,cityService.viewAll(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while view all the city ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/viewByName/{cityName}")
    public ResponseEntity<Response> viewByName(@PathVariable String cityName,@RequestHeader HttpHeaders httpHeader){
        logger.info("view city by name {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,cityService.viewByName(cityName),HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while view city by name ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Create")
    public ResponseEntity<Response> create(@RequestBody CityDto cityObject,@RequestHeader HttpHeaders httpHeader){
        logger.info("create new city {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,cityService.create(cityObject), HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while creating the city ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (ListOfValidationException e){
            logger.error("error occurred while creating the city ",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getCity")
    public ResponseEntity<Response> getCity(@RequestBody UUID id, @RequestHeader HttpHeaders httpHeader){
        logger.info("get the city for update {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,cityService.getCity(id),HttpStatus.OK);
        }
        catch (CustomValidationsException e){
            logger.error("error occurred while get the city for update ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);

        }
    }
    @PostMapping("/Update")
    public ResponseEntity<Response> update(@RequestBody CityDto cityDto,@RequestHeader HttpHeaders httpHeader){
        logger.info("update the new city {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,cityService.updateCity(cityDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while update the new city ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            logger.error("error occurred while update the new city ",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/Delete")
    public ResponseEntity<Response> delete(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeader){
        logger.info("delete the city {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,cityService.deleteCity(id),HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while delete the city  ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



}
