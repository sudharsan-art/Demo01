package com.app.employeedesk.service;


import com.app.employeedesk.dto.EducationQualificationDto;
import com.app.employeedesk.dto.EducationalDetailsDto;
import com.app.employeedesk.entity.EducationDetails;
import com.app.employeedesk.entity.EducationDetailsEmbedable;
import com.app.employeedesk.entity.EducationQualification;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EducationQualificationRepository;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.EducationDetailsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.EducationDetailsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EducationalDetailsService {
    private final  EducationDetailsRepository educationDetailsRepository;
    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;
    private final MessageService messageService;
    private final EducationDetailsValidation educationDetailsValidation;
    private final EducationQualificationRepository educationQualification;



    public EducationalDetailsDto entityToDto(EducationDetails educationDetails){
        Optional<EmployeeBasicDetails> basicDetails=employeeBasicDetailsRepository.findById(educationDetails.getId().getEmployeeBasicDetails().getId());
        if(basicDetails.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("employee.basic.details.id.invalid"));
        }
        return EducationalDetailsDto.builder()
                .employeeId(educationDetails.getId().getEmployeeBasicDetails().getId())
                .educationId(educationDetails.getId().getEducationQualification().getId())
                .fromDate(String.valueOf(educationDetails.getFromDate()))
                .toDate(String.valueOf(educationDetails.getToDate()))
                .institutionName(educationDetails.getInstitutionName())
                .educationId(educationDetails.getId().getEducationQualification().getId())
                .score(educationDetails.getScore())
                .scoreType(educationDetails.getScoreType())
                .build();
    }
    public List<EducationalDetailsDto> getEducationalDetails(UUID id){
        Optional<EmployeeBasicDetails> employeeBasicDetails=employeeBasicDetailsRepository.findById(id);
        List<EducationDetails> educationDetails=null;
        if(employeeBasicDetails.isPresent()){
             educationDetails= educationDetailsRepository.findByemployeeId(employeeBasicDetails.get());

        }else{
            throw new CustomValidationsException(messageService.messageResponse("no.employee.present"));
        }
        List<EducationalDetailsDto> a=new ArrayList<>();
        for(EducationDetails i:educationDetails){
            EducationalDetailsDto educationalDetailsDto=EducationalDetailsDto.builder()
                    .educationId(i.getId().getEducationQualification().getId())
                    .employeeId(i.getId().getEmployeeBasicDetails().getId())
                    .fromDate(String.valueOf(i.getFromDate()))
                    .toDate(String.valueOf(i.getToDate()))
                    .institutionName(i.getInstitutionName())
                    .score(i.getScore())
                    .scoreType(i.getScoreType())
                    .build();
            a.add(educationalDetailsDto);


        }

        return a;
    }
    public EducationalDetailsDto getUserEducationDetails(EducationalDetailsDto educationalDetailsDto){
        EducationDetailsEmbedable educationDetailsEmbedable=new EducationDetailsEmbedable();
        educationDetailsEmbedable.setEmployeeBasicDetails(employeeBasicDetailsRepository.findById(educationalDetailsDto.getEmployeeId()).orElseThrow(() ->new CustomValidationsException("error")));
        educationDetailsEmbedable.setEducationQualification(educationQualification.findById(educationalDetailsDto.getEducationId()).orElseThrow(() ->new CustomValidationsException("error")));
        Optional<EducationDetails> educationDetails=educationDetailsRepository.findById(educationDetailsEmbedable);
        if (educationDetails.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse("no.education.details.present"));
        }

        return educationDetails.map(this::entityToDto).get();
    }
    public String postEducationalDetails(EducationalDetailsDto educationDetails){
        List<String> errorMessage=new ArrayList<>();
        educationDetailsValidation.educationDetails(educationDetails,errorMessage);
        LocalDate fromDate=LocalDate.parse(educationDetails.getFromDate());
        LocalDate toDate=LocalDate.parse(educationDetails.getToDate());
        if(fromDate.isAfter(toDate)){
            errorMessage.add(messageService.messageResponse("from.date.to.date.invalid"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        EducationDetailsEmbedable educationDetailsEmbedable=new EducationDetailsEmbedable();
        educationDetailsEmbedable.setEmployeeBasicDetails(employeeBasicDetailsRepository.findById(educationDetails.getEmployeeId()).orElseThrow(() ->new CustomValidationsException("error")));
        educationDetailsEmbedable.setEducationQualification(educationQualification.findById(educationDetails.getEducationId()).orElseThrow(() ->new CustomValidationsException("error")));

        EducationDetails educationDetails1=EducationDetails.builder()
                .institutionName(educationDetails.getInstitutionName())
                .score(educationDetails.getScore())
                .scoreType(educationDetails.getScoreType())
                .fromDate(fromDate)
                .toDate(toDate)
                .id(educationDetailsEmbedable).build();
        educationDetailsRepository.save(educationDetails1);
        return messageService.messageResponse("education.saved.successfully");
    }

    public String deleteEducationDetails(EducationalDetailsDto educationalDetailsDto){
        EducationDetailsEmbedable educationDetailsEmbedable=new EducationDetailsEmbedable();
        educationDetailsEmbedable.setEmployeeBasicDetails(employeeBasicDetailsRepository.findById(educationalDetailsDto.getEmployeeId()).orElseThrow(() ->new CustomValidationsException("error")));
        educationDetailsEmbedable.setEducationQualification(educationQualification.findById(educationalDetailsDto.getEducationId()).orElseThrow(() ->new CustomValidationsException("error")));
        educationDetailsRepository.deleteById(educationDetailsEmbedable);
        return messageService.messageResponse("education.deleted.successfully");
    }
    public List<EducationQualificationDto> getQualificationDropdown() {

        List<EducationQualification> educationQualifications = educationQualification.findAll();
        List<EducationQualificationDto> educationQualificationDtos = new ArrayList<>();
        for (EducationQualification i : educationQualifications) {
            EducationQualificationDto a = EducationQualificationDto.builder()
                    .steam(i.getSteam())
                    .qualificationName(i.getQualificationName())
                    .steamYesNo(i.getSteamYesNo())
                    .id(i.getId())
                    .build();

            educationQualificationDtos.add(a);
        }
        return educationQualificationDtos;
    }














}
