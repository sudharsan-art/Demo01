package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.DepartmentType;
import com.app.employeedesk.enumeration.DesignationType;

import com.app.employeedesk.enumeration.GenderTypes;
import com.app.employeedesk.enumeration.WorkMode;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EmployeeAddressRepository;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.UserDetailsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.EmployeePersonalDetailsValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class EmployeePersonalDetailsService {
    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final EmployeeAddressRepository employeeAddressRepository;
    private final EmployeePersonalDetailsValidation employeePersonalDetailsValidation;
    public final MessageService messageService;
    public final CountryService countryService;
    public final StateService stateService;

    public EmployeePersonalDetailsTab personalDetailsGet(String userName){
        UserDetails userDetails=userDetailsRepository.findByUserNamevalue(userName);
        EmployeeBasicDetails employeeBasicDetails=userDetails.getEmployeeBasicDetails();
        EmployeeAddress employeeAddress=employeeAddressRepository.findByEmployeeBasicDetails(employeeBasicDetails);
        UserDetailsDto userDetailsDto1=UserDetailsDto.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .userName(userDetails.getUsername())
                .role(userDetails.getRole())
                .build();
        EmployeeBasicDetailsDto employeeBasicDetailsDto1=EmployeeBasicDetailsDto.builder()
                .id(employeeBasicDetails.getId())
                .email(employeeBasicDetails.getEmail())
                .dateOfBirth(employeeBasicDetails.getDateOfBirth())
                .employeecode(employeeBasicDetails.getEmployeecode())
                .desigination(String.valueOf(employeeBasicDetails.getDesigination()))
                .department(String.valueOf(employeeBasicDetails.getDepartment()))
                .workMode(WorkMode.HYBRID)
                .gender(String.valueOf(employeeBasicDetails.getGender()))
                .panCardNumber(employeeBasicDetails.getPanCardNumber())
                .profileImage(employeeBasicDetails.getProfileImage())
                .phoneNumber(employeeBasicDetails.getPhoneNumber())
                .firstName(employeeBasicDetails.getFirstName())
                .middleName(employeeBasicDetails.getMiddleName())
                .lastName(employeeBasicDetails.getLastName())
                .fatherName(employeeBasicDetails.getFatherName())
                .motherName(employeeBasicDetails.getMotherName())
                .dateOfJoining(employeeBasicDetails.getDateOfJoining())
                .build();
        EmployeeAddressDto employeeAddressDto1=null;
        if(employeeAddressRepository.existsByEmployeeBasicDetails(employeeBasicDetails)) {
            employeeAddressDto1 = EmployeeAddressDto.builder()
                    .id(employeeAddress.getId())
                    .city(employeeAddress.getCity())
                    .country(employeeAddress.getCountry())
                    .state(employeeAddress.getState())
                    .doorNo(employeeAddress.getDoorNo())
                    .landmark(employeeAddress.getLandmark())
                    .postalCode(employeeAddress.getPostalCode())
                    .street(employeeAddress.getStreet())
                    .build();
        }
        else {
            employeeAddressDto1 = EmployeeAddressDto.builder()
                    .city(null)
                    .country(null)
                    .state(null)
                    .doorNo(null)
                    .landmark(null)
                    .postalCode(null)
                    .street(null)
                    .build();
        }

        return EmployeePersonalDetailsTab.builder().userDetailsDto(userDetailsDto1)
                .employeeAddressDto(employeeAddressDto1)
                .employeeBasicDetailsDto(employeeBasicDetailsDto1)
                .build();

    }
    @Transactional
    public String employeePersonalDetailsUpdate(EmployeePersonalDetailsTab employeePersonalDetailsTab) {
        List<String> errorMessage = new ArrayList<>();
        employeePersonalDetailsValidation.personalDetailsTab(employeePersonalDetailsTab, errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }

        EmployeeBasicDetailsDto employeeBasicDetailsDto = employeePersonalDetailsTab.getEmployeeBasicDetailsDto();

        Optional<UserDetails> userDetails1 = userDetailsRepository.findById(employeePersonalDetailsTab.getUserDetailsDto().getId());
        if(userDetails1.isEmpty()){
            throw new CustomValidationsException("User Details repository not found");
        }

        Optional<EmployeeBasicDetails> optionalBasicDetails = employeeBasicDetailsRepository.findById(employeePersonalDetailsTab.getEmployeeBasicDetailsDto().getId());
        if (optionalBasicDetails.isPresent()) {
            EmployeeBasicDetails entityBasicDetails=optionalBasicDetails.get();
            entityBasicDetails.setId(employeeBasicDetailsDto.getId());
            entityBasicDetails.setDateOfJoining(employeeBasicDetailsDto.getDateOfJoining());
            entityBasicDetails.setDepartment(DepartmentType.valueOf(employeeBasicDetailsDto.getDepartment()));
            entityBasicDetails.setDesigination(DesignationType.valueOf(employeeBasicDetailsDto.getDesigination()));
            entityBasicDetails.setGender(GenderTypes.valueOf(employeeBasicDetailsDto.getGender()));
            entityBasicDetails.setPanCardNumber(employeeBasicDetailsDto.getPanCardNumber());
            entityBasicDetails.setProfileImage(employeeBasicDetailsDto.getProfileImage());
            entityBasicDetails.setFirstName(employeeBasicDetailsDto.getFirstName());
            entityBasicDetails.setLastName(employeeBasicDetailsDto.getLastName());
            entityBasicDetails.setMiddleName(employeeBasicDetailsDto.getMiddleName());
            entityBasicDetails.setFatherName(employeeBasicDetailsDto.getFatherName());
            entityBasicDetails.setMotherName(employeeBasicDetailsDto.getMotherName());

        }else {
            throw new CustomValidationsException("Basic details repository not found ");
        }
        EmployeeAddressDto employeeAddressDto = employeePersonalDetailsTab.getEmployeeAddressDto();
        Optional<EmployeeAddress> employeeAddress = employeeAddressRepository.findById(employeePersonalDetailsTab.getEmployeeAddressDto().getId());
        if (employeeAddress.isPresent()) {
            EmployeeAddress employeeAddress1=employeeAddress.get();
            employeeAddress1.setDoorNo(employeeAddressDto.getDoorNo());
            employeeAddress1.setPostalCode(employeeAddressDto.getPostalCode());
            employeeAddress1.setLandmark(employeeAddressDto.getLandmark());
            employeeAddress1.setStreet(employeeAddressDto.getStreet());
            employeeAddress1.setCountry(employeeAddressDto.getCountry());
            employeeAddress1.setCity(employeeAddressDto.getCity());
            employeeAddress1.setState(employeeAddressDto.getState());

        }else{
            throw new CustomValidationsException("Employee Address repository not found");
        }
    if(optionalBasicDetails.isPresent() && employeeAddress.isPresent() && userDetails1.isPresent()) {
        employeeBasicDetailsRepository.save(optionalBasicDetails.get());
        employeeAddressRepository.save(employeeAddress.get());
        userDetailsRepository.save(userDetails1.get());
    }
        return messageService.messageResponse("updated.successfully");




    }

    public Optional<EmployeeBasicDetails> getById(UUID id){
        return employeeBasicDetailsRepository.findById(id);
    }


    public EmployeeBasicDetails findEmployeeById(UUID id) throws CustomValidationsException{
        return employeeBasicDetailsRepository.findById(id).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("employee.details.empty")));
    }

    public List<EmployeeBasicDetailsViewDto> getAllEmployeeDetails(){
        List<EmployeeBasicDetails> employeeBasicDetails=employeeBasicDetailsRepository.findAll();
        if(employeeBasicDetails.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("employee.details.empty"));
        }
        List<EmployeeBasicDetailsViewDto> employeeBasicDetailsViewDto=new ArrayList<>();
        for(EmployeeBasicDetails i:employeeBasicDetails){
            employeeBasicDetailsViewDto.add(EmployeeBasicDetailsViewDto.builder()
                            .employeeId(i.getId())
                            .employeeName(i.getFirstName())
                            .employeeCode(i.getEmployeecode())
                            .designation(i.getDesigination())
                            .department(i.getDepartment())
                            .build());
        }
        return employeeBasicDetailsViewDto;
    }

    public EmployeeBasicDetails getEmployeeDetailsByPrinciple(Principal principal){
        String userName= principal.getName();
        UserDetails userDetails=userDetailsRepository.findByUserNamevalue(userName);
        return userDetails.getEmployeeBasicDetails();
    }
    public String getEmployeeNameByPricipal(Principal principal){
        String userName= principal.getName();
        UserDetails userDetails=userDetailsRepository.findByUserNamevalue(userName);
        return userDetails.getEmployeeBasicDetails().getFirstName()+" "+userDetails.getEmployeeBasicDetails().getLastName();

    }
     public List<UUID> getAllEmployeeId(){
        return employeeBasicDetailsRepository.findByIdActiveEmployee();
     }
     public  SiftAndWeekOfDetailsDto findSiftAndWeekDetails(UUID empId){
        return employeeBasicDetailsRepository.findByWeekAndSiftDetails(empId);
     }


    public UUID getEmployeeDetailsByPrinciples(Principal principal){
        String userName= principal.getName();
        UserDetails userDetails=userDetailsRepository.findByUserNamevalue(userName);
        return userDetails.getEmployeeBasicDetails().getId();
    }



    public FindEmployeeNameDto getEmployeeId(String employeeId) {
        return employeeBasicDetailsRepository.findByIdEmployeeCode(UUID.fromString(employeeId));
    }





}

