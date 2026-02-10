package com.app.employeedesk.controller;


import com.app.employeedesk.auth.AuthenticationRequest;
import com.app.employeedesk.auth.AuthenticationService;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
@Tag(name = "Authentication_Controller")
public class AuthenticationController {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final AuthenticationService service;


    private final ResponseGenerator responseGenerator;



    @PostMapping("/authenticate")
    public ResponseEntity<Response> login(@RequestBody AuthenticationRequest request,@RequestHeader HttpHeaders httpHeader) {
        logger.info("generate token  {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context,service.authenticate(request), HttpStatus.OK);
        }catch (AuthenticationException e){
            logger.error("error occurred while generate token ",e);
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
