package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeeWorkExperienceDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeWorkExperience;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.EmployeeExperienceRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.validation.EmployeeExperienceValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeExperienceService {
    private final EmployeeExperienceRepository employeeExperienceRepository;
    private final EmployeeExperienceValidation employeeExperienceValidation;
    private final MessageService  messageService;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;

    public EmployeeWorkExperienceDto entityToDto(EmployeeWorkExperience employeeWorkExperience){
        return EmployeeWorkExperienceDto.builder()
                .id(employeeWorkExperience.getId())
                .experience(employeeWorkExperience.getExperience())
                .role(employeeWorkExperience.getRole())
                .companyLocation(employeeWorkExperience.getCompanyLocation())
                .companyName(employeeWorkExperience.getCompanyName())
                .dateOfJoining(String.valueOf(employeeWorkExperience.getDateOfJoining()))
                .dateOfReleaving(String.valueOf(employeeWorkExperience.getDateOfReleaving()))
                .employeeBasicDetailsId(employeeWorkExperience.getEmployeeBasicDetails().getId())
                .build();
    }


    public Object getEmployeeWorkExperience(UUID id){
        Optional<EmployeeBasicDetails> employeeBasicDetails=employeePersonalDetailsService.getById(id);
        List<EmployeeWorkExperience> employeeExperience=null;
        if(employeeBasicDetails.isPresent()) {
            employeeExperience= employeeExperienceRepository.findByEmployeeBasicDetails(employeeBasicDetails.get());
        }
        List<EmployeeWorkExperienceDto> employeeWorkExperienceDtos;
        if(employeeExperience==null){
            return messageService.messageResponse("no.experience.available");
        }
        employeeWorkExperienceDtos= employeeExperience.stream().map(this::entityToDto).toList();
        return employeeWorkExperienceDtos;
    }
    public EmployeeWorkExperienceDto updateEmployeeExperience(UUID id){
        Optional<EmployeeWorkExperience> employeeWorkExperience=employeeExperienceRepository.findById(id);
        return employeeWorkExperience.map(this::entityToDto).orElse(null);


    }
    public String addexperience(EmployeeWorkExperienceDto employeeWorkExperienceDto){
        List<String> errorMessage=new ArrayList<>();
        employeeExperienceValidation.employeeExperience(employeeWorkExperienceDto,errorMessage);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate joindate=LocalDate.parse(employeeWorkExperienceDto.getDateOfJoining(),formatter);
        LocalDate releavingDate=LocalDate.parse(employeeWorkExperienceDto.getDateOfReleaving(),formatter);
        if(joindate.isAfter(releavingDate)){
             errorMessage.add(messageService.messageResponse("joindate.releaving.date.notvalid"));
        }
        if (!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        Period experience=Period.between(joindate,releavingDate);
        EmployeeWorkExperience employeeWorkExperience= EmployeeWorkExperience.builder()
                .id(employeeWorkExperienceDto.getId())
                .companyName(employeeWorkExperienceDto.getCompanyName())
                .companyLocation(employeeWorkExperienceDto.getCompanyLocation())
                .role(employeeWorkExperienceDto.getRole())
                .experience(experience.getYears())
                .dateOfJoining(DateUtil.parseDate(employeeWorkExperienceDto.getDateOfJoining()))
                .dateOfReleaving(DateUtil.parseDate(employeeWorkExperienceDto.getDateOfReleaving()))
                .employeeBasicDetails(employeeBasicDetailsRepository.findById(employeeWorkExperienceDto.getEmployeeBasicDetailsId()).get())
                .build();
        employeeExperienceRepository.save(employeeWorkExperience);
        return messageService.messageResponse("saved.success.experience");
    }
    public String deleteExperience(UUID id){
        employeeExperienceRepository.deleteById(id);
        return messageService.messageResponse("deleted.success.experience");
    }



}
