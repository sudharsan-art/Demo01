package com.app.employeedesk.validation;
import com.app.employeedesk.dto.EmployeeAddressDto;
import com.app.employeedesk.dto.EmployeePersonalDetailsTab;
import com.app.employeedesk.enumeration.GenderTypes;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EmployeePersonalDetailsValidation {
    public final BasicValidation basicValidation;
    public final MessageService messageService;
    public void personalDetailsTab(EmployeePersonalDetailsTab employeePersonalDetailsTab, List<String> errorMessage){
        String regex = "^[a-z0-9+_.-]+@(.+)$";
        Pattern pattern=Pattern.compile(regex);
        com.app.employeedesk.dto.EmployeeBasicDetailsDto employeeBasicDetailsDto=employeePersonalDetailsTab.getEmployeeBasicDetailsDto();
        EmployeeAddressDto employeeAddressDto=employeePersonalDetailsTab.getEmployeeAddressDto();
        if(!basicValidation.stringValidation(employeeBasicDetailsDto.getFirstName())){
            errorMessage.add(messageService.messageResponse("first.name.incorrect"));
        }
        if(!basicValidation.stringValidation(employeeBasicDetailsDto.getMiddleName())){
            errorMessage.add(messageService.messageResponse("middle.name.incorrect"));
        }
        if(!basicValidation.stringValidation(employeeBasicDetailsDto.getLastName())){
            errorMessage.add(messageService.messageResponse("last.name.incorrect"));
        }
        if(!basicValidation.stringValidation(employeeBasicDetailsDto.getFatherName())){
            errorMessage.add(messageService.messageResponse("father.name.incorrect"));
        }
        if(!basicValidation.stringValidation(employeeBasicDetailsDto.getMotherName())){
            errorMessage.add(messageService.messageResponse("mother.name.incorrect"));
        }

        if(employeeBasicDetailsDto.getGender() == null ){
            errorMessage.add(messageService.messageResponse("gender.not.valid"));
        }
        if(employeeBasicDetailsDto.getPanCardNumber()==null||employeeBasicDetailsDto.getPanCardNumber().isBlank()||employeeBasicDetailsDto.getPanCardNumber().length()!=10){
            errorMessage.add(messageService.messageResponse("pancard.invalid"));
        }
        if(!basicValidation.stringValidation(employeeAddressDto.getLandmark())){
            errorMessage.add(messageService.messageResponse("landmark.not.valid"));
        }
        if(!basicValidation.stringValidation(employeeAddressDto.getStreet())){
            errorMessage.add(messageService.messageResponse("street.not.valid"));
        }
        if(employeeAddressDto.getDoorNo()==null){
            errorMessage.add(messageService.messageResponse("door.no.not.valid"));
        }
        if (employeeAddressDto.getPostalCode().toString().length()!=6){
            errorMessage.add(messageService.messageResponse("postal.code.invalid"));
        }
        if(!pattern.matcher(employeeBasicDetailsDto.getEmail()).matches()){
            errorMessage.add(messageService.messageResponse("email.invalid"));
        }
        Pattern p = Pattern.compile("^\\d{10}$");
        
        if(!p.matcher(employeeBasicDetailsDto.getPhoneNumber()).matches()){
            errorMessage.add(messageService.messageResponse("phonenumber.invalid"));
        }

    }

}
