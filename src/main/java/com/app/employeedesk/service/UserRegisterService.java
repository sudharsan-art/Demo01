package com.app.employeedesk.service;

import com.app.employeedesk.dto.AdminRegisterDto;
import com.app.employeedesk.dto.EmployeeBasicDetailsViewDto;
import com.app.employeedesk.dto.EmployeeRegisterDto;
import com.app.employeedesk.dto.EmployeeWeekOffDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeShiftEntity;
import com.app.employeedesk.entity.EmployeeWeekOff;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.enumeration.DepartmentType;
import com.app.employeedesk.enumeration.DesignationType;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.enumeration.UserType;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.UserDetailsRepository;
import com.app.employeedesk.response.MessageService;

import com.app.employeedesk.util.IConstant;

import com.app.employeedesk.util.DateUtil;

import com.app.employeedesk.validation.EmployeeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserDetailsRepository repository;

    private final MessageService messageService;

    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeValidation employeeValidation;

    Integer value=0;
    String prefix = "EB";
    List<Integer> num=new ArrayList<>();

    @Transactional
    public String employeeRegister(EmployeeRegisterDto request) {
        List<String> errorMessage = new ArrayList<>();
        employeeValidation.employeeDetailsValidation(request,errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }
        String password=request.getEmail().substring(0,3)+request.getPhoneNumber().substring(0,3);
        List<EmployeeWeekOff> employeeWeekOffs=new ArrayList<>();
        for(EmployeeWeekOffDto i:request.getEmployeeShiftDto().getEmployeeWeekOff()){
            employeeWeekOffs.add(EmployeeWeekOff.builder()
                    .weekNo(i.getWeekNo())
                    .Day(i.getDay())
                    .dayStatus(i.getDayStatus())
                    .build());
        }
        EmployeeShiftEntity employeeShiftEntity=EmployeeShiftEntity.builder()
                .id(request.getEmployeeShiftDto().getId())
                .remarks(request.getEmployeeShiftDto().getRemarks())
                .shiftName(request.getEmployeeShiftDto().getShiftName())
                .shiftStartTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getShiftStartTime()))
                .shiftEndTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getShiftEndTime()))
                .firstHalfStartTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getFirstHalfStartTime()))
                .firstHalfEndTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getFirstHalfEndTime()))
                .secoundHalfStartTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getSecoundHalfStartTime()))
                .secoundHalfEndTime(DateUtil.parseLocalTime(request.getEmployeeShiftDto().getSecoundHalfEndTime()))
                .employeeWeekOff(employeeWeekOffs)
                .build();


        var employeeDetails= EmployeeBasicDetails.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .employeecode(prefix + generateEmployeeId().toString())
                .department(DepartmentType.valueOf(request.getDepartment()))
                .desigination(DesignationType.valueOf(request.getDesignation()))
                .employeeShiftEntity(employeeShiftEntity)
                .build();



        var userDetails = UserDetails.builder()
                .name(request.getFirstName()+" "+request.getLastName())
                .password(passwordEncoder.encode(password))
                .phoneNumber(request.getPhoneNumber())
                .userName(request.getEmail())
                .status(Status.ACTIVE)
                .role(UserType.USER)

                .employeeBasicDetails(employeeDetails)
                .build();
        repository.save(userDetails);
        employeeBasicDetailsRepository.save(employeeDetails);
        return messageService.messageResponse("user.details.save");
    }

    public String adminRegister(AdminRegisterDto request) {
        List<String> errorMessage=new ArrayList<>();
        employeeValidation.adminValidation(request,errorMessage);
        if(!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }

        var userDetails = UserDetails.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userName(request.getEmail())
                .status(Status.ACTIVE)
                .role(UserType.ADMIN)
                .build();
        repository.save(userDetails);
        return messageService.messageResponse("user.details.save");
    }

    private synchronized Integer generateEmployeeId() {

        if(value==0) {
            List<EmployeeBasicDetails> employeeBasicDetails = employeeBasicDetailsRepository.findAll();
            if(employeeBasicDetails.isEmpty()){
                value=value+1;
            }
            else{
                for (EmployeeBasicDetails i : employeeBasicDetails) {
                    if(i.getEmployeecode()==null){
                        i.setEmployeecode("EB1");
                    }
                    int number = Integer.parseInt(i.getEmployeecode().substring(2));
                    num.add(number);
                }
                value= Collections.max(num)+1;
                num.clear();
            }
        }
        else {
            value = value + 1;
        }
        return value;
    }

    public EmployeeBasicDetailsViewDto getEmployeeDetailsByPrinciple(Principal principal){
        String userName= principal.getName();
        return repository.findUserDetailsByUserName(userName).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("employee.details.empty")));
    }

}
