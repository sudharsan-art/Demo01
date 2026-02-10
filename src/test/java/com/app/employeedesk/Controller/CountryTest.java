package com.app.employeedesk.Controller;

import com.app.employeedesk.controller.CountryController;
import com.app.employeedesk.dto.CountryDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CountryService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@ExtendWith({MockitoExtension.class})

public class CountryTest {

    @InjectMocks
    CountryController countryController;


    @Mock
     CountryService countryService;
    @Mock
    ResponseGenerator responseGenerator;
    @Mock
    HttpHeaders httpHeaders;
    @Mock
    Exception e;
    @Mock
    CountryDto countryDto;




    @Test
    public void countryTestSucess() throws Exception {
        when (countryService.countryCreate(any())).thenReturn("Country created successfully.");
        TransactionContext context = new TransactionContext();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(responseGenerator.successResponse(any(),any(),any()))
                .thenReturn(successResponse());
        ResponseEntity<Response> result= countryController.countryCreate(getCountryDtoObj(),httpHeaders);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void countryTestError() throws Exception{
        TransactionContext context=new TransactionContext();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.countryCreate(any())).thenThrow(new CustomValidationsException("Not proper format"));
        when(responseGenerator.errorResponse(any(),any(),any())).thenReturn(errorResponse());
        ResponseEntity<Response> result=countryController.countryCreate(getCountryDtoObj(),httpHeaders);
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

    }
    @Test
    public void viewAllTest() throws CustomValidationsException {
        TransactionContext context = new TransactionContext();
        List<CountryDto> countries = Collections.singletonList(getCountryDtoObj());

        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.viewAll()).thenReturn(countries);
        when(responseGenerator.successResponse(any(), any(), any())).thenReturn(successResponse());

        ResponseEntity<Response> result = countryController.viewall(httpHeaders);

        assertEquals(HttpStatus.OK, result.getStatusCode());


    }
    @Test
    public void viewbyName(){
        TransactionContext context=new TransactionContext();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.viewByName("India")).thenReturn(getCountryDtoObj());
        when(responseGenerator.successResponse(any(), any(), any())).thenReturn(successResponse());
        ResponseEntity<Response> result = countryController.viewByName("India",httpHeaders);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void  getCountry(){

        MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);
        TransactionContext context=new TransactionContext();
        UUID uuid=UUID.randomUUID();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.getCountry(uuid)).thenReturn(getCountryDtoObj());
        when(responseGenerator.successResponse(any(), any(), any())).thenReturn(successResponse());
        ResponseEntity<Response> result = countryController.getCountry(uuid,httpHeaders);
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }
    @Test
     void delete(){
        TransactionContext context=new TransactionContext();
        UUID uuid=UUID.randomUUID();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.deleteCountry(uuid)).thenReturn("deleted successfully");
        when(responseGenerator.successResponse(any(),any(),any())).thenReturn(successResponse());
        ResponseEntity<Response> result = countryController.deleteCountry(uuid,httpHeaders);
        assertEquals(HttpStatus.OK,result.getStatusCode());

    }
    @Test
    void deletefail(){
        TransactionContext context=new TransactionContext();
        UUID uuid=UUID.randomUUID();
        when(responseGenerator.generateTransationContext(any())).thenReturn(context);
        when(countryService.deleteCountry(uuid)).thenThrow(new CustomValidationsException("Delete failed"));
        when(responseGenerator.errorResponse(any(),any(),any())).thenReturn(errorResponse());
        ResponseEntity<Response> result = countryController.deleteCountry(uuid,httpHeaders);
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());

    }


    public ResponseEntity<Response> successResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Response response = new Response();
        response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers,HttpStatus.OK);
        return responseEntity;
    }
    public ResponseEntity<Response> errorResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Response response = new Response();
        response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers,HttpStatus.BAD_REQUEST);
        return responseEntity;
    }
    public CountryDto getCountryDtoObj(){
        return CountryDto.builder().name("Test").asName("").phoneCode("00").build();
    }
}