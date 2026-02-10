package com.app.employeedesk.validation;

import com.app.employeedesk.dto.AdminRegisterDto;
import com.app.employeedesk.dto.EmployeeRegisterDto;

import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.UserDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EmployeeValidation {

    private final MessageService messageService;
    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final BasicValidation basicValidation;

//    public void employeeDetailsValidation(EmployeeRegisterDto employeeRegisterDto, List<String> errorMessage){
//        if(employeeRegisterDto.getFirstName()==null|| employeeRegisterDto.getFirstName().isBlank()|| employeeRegisterDto.getFirstName().length()<3|| employeeRegisterDto.getFirstName().length()>100){
//            errorMessage.add(messageService.messageResponse("firstName.not.proper.format"));
//        }
//        if (employeeBasicDetailsRepository.existsByEmail(employeeRegisterDto.getEmail())){
//            errorMessage.add(messageService.messageResponse("email.duplicates"));
//        }
//        String regex = "^[a-z0-9+_.-]+@(.+)$";
//        Pattern pattern=Pattern.compile(regex);
//        if (!pattern.matcher(employeeRegisterDto.getEmail()).matches()) {
//            errorMessage.add(messageService.messageResponse("email.not.valid"));
//        }
//        Pattern p = Pattern.compile("^\\d{10}$");
//        if(!p.matcher(employeeRegisterDto.getPhoneNumber()).matches()){
//            errorMessage.add(messageService.messageResponse("phone.number.not.valid"));
//        }
//        if(employeeBasicDetailsRepository.existsByPhoneNumber(employeeRegisterDto.getPhoneNumber())){
//            errorMessage.add(messageService.messageResponse("phone.number.not.valid"));
//        }
//        if(employeeRegisterDto.getDepartment()==null|| employeeRegisterDto.getDepartment().isBlank()){
//            errorMessage.add(messageService.messageResponse("department.not.valid"));
//        }
//        if(employeeRegisterDto.getDesignation()==null|| employeeRegisterDto.getDesignation().isBlank()){
//            errorMessage.add(messageService.messageResponse("designation.not.valid"));
//        }
//    }
//
//>>>>>>> master

    public void employeeDetailsValidation(EmployeeRegisterDto employeeRegisterDto, List<String> errorMessage){
        if(employeeRegisterDto.getFirstName()==null|| employeeRegisterDto.getFirstName().isBlank()|| employeeRegisterDto.getFirstName().length()<3|| employeeRegisterDto.getFirstName().length()>100){
            errorMessage.add(messageService.messageResponse("firstName.not.proper.format"));
        }
        if (employeeBasicDetailsRepository.existsByEmail(employeeRegisterDto.getEmail())){
            errorMessage.add(messageService.messageResponse("email.duplicates"));
        }
        String regex = "^[a-z0-9+_.-]+@(.+)$";
        Pattern pattern=Pattern.compile(regex);
        if (!pattern.matcher(employeeRegisterDto.getEmail()).matches()) {
            errorMessage.add(messageService.messageResponse("email.not.valid"));
        }
        Pattern p = Pattern.compile("^\\d{10}$");
        if(!p.matcher(employeeRegisterDto.getPhoneNumber()).matches()){
            errorMessage.add(messageService.messageResponse("phone.number.not.valid"));
        }
        if(employeeBasicDetailsRepository.existsByPhoneNumber(employeeRegisterDto.getPhoneNumber())){
            errorMessage.add(messageService.messageResponse("phone.number.not.valid"));
        }
        if(employeeRegisterDto.getDepartment()==null|| employeeRegisterDto.getDepartment().isBlank()){
            errorMessage.add(messageService.messageResponse("department.not.valid"));
        }
        if(employeeRegisterDto.getDesignation()==null|| employeeRegisterDto.getDesignation().isBlank()){
            errorMessage.add(messageService.messageResponse("designation.not.valid"));
        }
        if(employeeRegisterDto.getEmployeeShiftDto()==null){
            errorMessage.add(messageService.messageResponse("shift.cannot.null"));
        }
    }

    public void adminValidation(AdminRegisterDto dto,List<String> errorMessage){
        if (!basicValidation.stringValidation(dto.getName())) {
            errorMessage.add("name.incorrect");
        }
        if(userDetailsRepository.adminDuplicate(dto.getName())){
            errorMessage.add(messageService.messageResponse("admin.name.duplicate"));
        }
        if(!basicValidation.stringValidation(dto.getEmail()) || userDetailsRepository.existsByUserName(dto.getEmail())){
            errorMessage.add(messageService.messageResponse("email.invalid"));
        }
        if (dto.getPhoneNumber() != null) {
            errorMessage.add(messageService.messageResponse("phone.invalid"));
        }
        if(dto.getPassword() != null){
            errorMessage.add(messageService.messageResponse("password.invalid"));
        }



    }

}
