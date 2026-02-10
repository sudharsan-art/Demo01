package com.app.employeedesk.service;

import com.app.employeedesk.dto.CourseMaterialsOutputDto;
import com.app.employeedesk.dto.EmployeeMaterialReferenceLinkViewDto;
import com.app.employeedesk.dto.EmployeeSubTopicReferenceLinkDto;
import com.app.employeedesk.entity.EmployeeCourseSubTopicDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeeCourseSubTopicDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeCourseSubTopicDetailsService {

    private final EmployeeCourseSubTopicDetailsRepository employeeCourseSubTopicDetailsRepository;

    private final CourseMaterialsService courseMaterialsService;

    private final CourseReferenceLinkService courseReferenceLinkService;

    private final MessageService messageService;

    public EmployeeMaterialReferenceLinkViewDto getEmployeeMaterialAndReferenceLink(String employeeSubtopicId){
        EmployeeCourseSubTopicDetails employeeCourseSubTopicDetails=employeeCourseSubTopicDetailsRepository.findById(UUID.fromString(employeeSubtopicId)).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("employee.subTopic.empty")));
       List<CourseMaterialsOutputDto> courseMaterialsOutputDto= courseMaterialsService.getAllMaterialDetails(employeeCourseSubTopicDetails.getCourseSubTopicDetails().getId());
       List<EmployeeSubTopicReferenceLinkDto> employeeSubTopicReferenceLinkDto=courseReferenceLinkService.findAllReferenceLinkDetails(employeeCourseSubTopicDetails.getCourseSubTopicDetails().getId());
        return  EmployeeMaterialReferenceLinkViewDto.builder()
                .materialsOutputDto(courseMaterialsOutputDto)
                .employeeSubTopicReferenceLinkDto(employeeSubTopicReferenceLinkDto)
                .build();
    }

    public EmployeeCourseSubTopicDetails findById(UUID employeeSubTopicDetails){
        return employeeCourseSubTopicDetailsRepository.findById(employeeSubTopicDetails).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("")));
    }


}
