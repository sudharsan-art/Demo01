package com.app.employeedesk.Controller;

import com.app.employeedesk.controller.StateController;
import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.repo.StateRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.StateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class State {
    @InjectMocks
    StateController stateController;
    @Mock
    StateService stateService;
    @Mock
    StateRepository stateRepository;
    @Mock
    MessageService messageService;
    @Mock
    ResponseGenerator responseGenerator;
    @Mock
    HttpHeaders httpHeaders;

    public StateDto createStateDtoObject() {
       return StateDto.builder()
                .id(UUID.randomUUID())
                .countryName("INDIA")
                .name("Tamil Nadu")
                .asName("TN")
                .country(UUID.randomUUID())
                .build();
    }



    @Test
    public void stateCreate(){
        TransactionContext context=new TransactionContext();

        when(responseGenerator.generateTransationContext(any(HttpHeaders.class))).thenReturn(context);
        when(stateService.create(any(StateDto.class))).thenReturn("saved successfully");
        when(responseGenerator.successResponse(eq(context), eq("saved successfully"), eq(HttpStatus.OK)))
                .thenReturn(successResponse());
        ResponseEntity<Response> response=stateController.create(createStateDtoObject(),httpHeaders);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());

    }
    @Test
    public void stateUpdate(){
        TransactionContext context=new TransactionContext();
        when(responseGenerator.generateTransationContext(httpHeaders)).thenReturn(context);
        when(stateService.update(any(StateDto.class))).thenReturn("updated successfully");
        when(responseGenerator.successResponse(eq(context),eq("updated successfully"),eq(HttpStatus.OK))).thenReturn(successResponse());
        ResponseEntity<Response> response=stateController.update(createStateDtoObject(),httpHeaders);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    public void stateDelete(){
        TransactionContext context=new TransactionContext();
        when(responseGenerator.generateTransationContext(httpHeaders)).thenReturn(context);
        when(stateService.delete(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"))).thenReturn("deleted successfully");
        when(responseGenerator.successResponse(eq(context),eq("deleted successfully"),eq(HttpStatus.OK))).thenReturn(successResponse());
        ResponseEntity<Response> response=stateController.delete(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),httpHeaders);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    public ResponseEntity<Response> successResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Response response = new Response();
        response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, HttpStatus.OK);
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

}
