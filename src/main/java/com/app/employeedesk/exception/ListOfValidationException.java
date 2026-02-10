package com.app.employeedesk.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ListOfValidationException extends RuntimeException{
    private final List<String> errorMessages;

    public ListOfValidationException(List<String> errorMessages){
        this.errorMessages=errorMessages;
    }

}
