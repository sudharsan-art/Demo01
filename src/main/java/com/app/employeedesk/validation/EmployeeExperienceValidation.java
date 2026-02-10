package com.app.employeedesk.validation;

import com.app.employeedesk.dto.EmployeeWorkExperienceDto;
import com.app.employeedesk.entity.EmployeeWorkExperience;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeExperienceValidation {
    private final BasicValidation basicValidation;
    private final MessageService messageService;
    public void employeeExperience(EmployeeWorkExperienceDto employeeWorkExperience, List<String> errorMessage){
        if(!basicValidation.stringValidation(employeeWorkExperience.getRole())){
            errorMessage.add(messageService.messageResponse("role.invalid"));
        }
        if(!basicValidation.stringValidation(employeeWorkExperience.getCompanyLocation())){
            errorMessage.add(messageService.messageResponse("location.invalid"));
        }
        if(!basicValidation.stringValidation(employeeWorkExperience.getCompanyName())){
            errorMessage.add(messageService.messageResponse("company.name.invalid"));
        }


    }
}
