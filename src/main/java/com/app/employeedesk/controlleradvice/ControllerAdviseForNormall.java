package com.app.employeedesk.controlleradvice;

import com.app.employeedesk.exception.TimeIncorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;

@ControllerAdvice
public class ControllerAdviseForNormall {

    @ExceptionHandler(TimeIncorrectException.class)
    public ResponseEntity<?> timeIncorrectException(TimeIncorrectException exception, WebRequest request){
        ErrorDetails details=new ErrorDetails(LocalTime.now(), exception.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);

    }
}
