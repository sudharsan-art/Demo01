package com.app.employeedesk.validation;

import com.app.employeedesk.dto.EducationalDetailsDto;
import com.app.employeedesk.entity.EducationDetails;
import com.app.employeedesk.repo.EducationDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EducationDetailsValidation {
    public MessageService messageService;
    public final BasicValidation basicValidation;
    public EducationDetailsRepository educationDetailsRepository;

    public void educationDetails(EducationalDetailsDto educationDetails, List<String> errorMessage){
        if(!basicValidation.stringValidation(educationDetails.getInstitutionName())){
            errorMessage.add(messageService.messageResponse("institution.invalid"));
        }

        if(!(educationDetails.getScoreType().equalsIgnoreCase("Total")||educationDetails.getScoreType().equalsIgnoreCase("percentage")
        ||educationDetails.getScoreType().equalsIgnoreCase("CGPA")||educationDetails.getScoreType().equalsIgnoreCase("Grade"))){
            errorMessage.add(messageService.messageResponse("education.invalid"));
        }


    }
}
