package com.app.employeedesk.validation;

import com.app.employeedesk.dto.EmployeeBasicDetailsDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PhoneNumberEmailValidation {
    private MessageService messageService;
    private EmployeeBasicDetailsRepository employeeBasicDetailsRepository;

    public Boolean phoneNumberValidation(EmployeeBasicDetailsDto employeeBasicDetailsDto, EmployeeBasicDetails employeeBasicDetails) {
        if (!employeeBasicDetails.getPhoneNumber().equals(employeeBasicDetailsDto.getPhoneNumber())) {
            return employeeBasicDetailsRepository.existsByPhoneNumber(employeeBasicDetailsDto.getPhoneNumber());
        }
        else {
            return employeeBasicDetailsRepository.existsByPhoneNumber(employeeBasicDetailsDto.getPhoneNumber());
        }

    }

    public boolean emailValidation(EmployeeBasicDetailsDto employeeBasicDetailsDto, EmployeeBasicDetails employeeBasicDetails){
        if (!employeeBasicDetails.getEmail().equals(employeeBasicDetailsDto.getEmail())) {
            return employeeBasicDetailsRepository.existsByEmail(employeeBasicDetailsDto.getEmail());
        }
        else {
            return employeeBasicDetailsRepository.existsByEmail(employeeBasicDetailsDto.getEmail());
        }

    }
    
}

