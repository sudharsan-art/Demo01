package com.app.employeedesk.exception;


import jakarta.validation.ValidationException;

public class CustomValidationsException extends ValidationException {



    public CustomValidationsException(String s) {
        super(s);
    }

    public CustomValidationsException() {
        super();
    }

    public CustomValidationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomValidationsException(Throwable cause) {
        super(cause);
    }

}




