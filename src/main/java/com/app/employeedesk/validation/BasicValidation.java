package com.app.employeedesk.validation;

import org.springframework.stereotype.Component;

@Component
public class BasicValidation {
    public boolean stringValidation(String  string){
        if(string==null ||string.isBlank()){
            return false;
        }
        return string.length() >= 3 && string.length() <= 255;

    }


}
