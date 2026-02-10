package com.app.employeedesk.controller;
import com.app.employeedesk.dto.CountryDto;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CountryService;
import com.app.employeedesk.validation.MasterValidation;
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
@RequestMapping("/Country")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;
    private final MasterValidation masterValidation;
    private final Logger logger=LoggerFactory.getLogger(getClass());
    private final ResponseGenerator  responseGenerator;


    @GetMapping("/viewall")
    public ResponseEntity<Response> viewall( @RequestHeader HttpHeaders httpHeader){
        logger.info("view all country {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return  responseGenerator.successResponse(context,countryService.viewAll(),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while viewing all country ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/viewByName/{countryName}")
    public ResponseEntity<Response> viewByName(@PathVariable String countryName, @RequestHeader HttpHeaders httpHeader){
        logger.info("viewing by the country Name {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,countryService.viewByName(countryName),HttpStatus.OK);
        }catch (Exception e){
            logger.error("error occurred while viewing by the country Name",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/create")
    public ResponseEntity<Response> countryCreate(@RequestBody CountryDto countryObject, @RequestHeader HttpHeaders httpHeader){
        logger.info("create new Country {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,countryService.countryCreate(countryObject),HttpStatus.OK);
        }
        catch (CustomValidationsException e){
            logger.error("error occurred while creating the country ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);

        }
        catch (ListOfValidationException e){
            logger.error("error occurred while creating the countyr ",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);

        }


    }
    @GetMapping("/getCountry")
    public ResponseEntity<Response> getCountry(@RequestBody  UUID id, @RequestHeader HttpHeaders httpHeader){
        logger.info("getting the country for updating {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,countryService.getCountry(id),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while getting the country for updating ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Response>updateCountry(@RequestBody CountryDto countrydto, @RequestHeader HttpHeaders httpHeader){
        logger.info("updating the country {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,countryService.updateCountry(countrydto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while updating the country",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (ListOfValidationException e){
            logger.error("error occurred while updating the country",e);
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteCountry(@RequestBody UUID id, @RequestHeader HttpHeaders httpHeader){
        logger.info("deleting the country  {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            return responseGenerator.successResponse(context,countryService.deleteCountry(id),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.error("error occurred while deleting the country ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
