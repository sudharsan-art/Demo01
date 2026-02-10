package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.CourseDetails;
import com.app.employeedesk.entity.CourseTopicDetails;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CourseDetailsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.IConstant;
import com.app.employeedesk.validation.CourseMasterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseDetailsService {

    private final CourseDetailsRepository courseDetailsRepository;

    private final MessageService messageService;

    private final CourseMasterValidation courseValidation;

    public MessageDto deleteCourse(UUID courseId) {
        courseDetailsRepository.deleteById(courseId);
        return MessageDto.builder()
                .message(messageService.messageResponse("course.details.delete"))
                .build();
    }

    public CourseDetails buildCourseDetails(CourseDetailsInputDto courseDetailsInputDto){
        return CourseDetails.builder()
                .courseName(courseDetailsInputDto.getCourseName())
                .description(courseDetailsInputDto.getDescription())
                .status(Status.ACTIVE)
                .build();
    }

    public void courseDetailsWithIdValidation(CourseDetailsInputDto courseDetailsInputDto){
        CourseDetails courseDetails=courseDetailsRepository.findByCourseName(courseDetailsInputDto.getCourseName());
        if (courseDetails!=null && !Objects.equals(courseDetails.getId().toString(), courseDetailsInputDto.getCourseId())) {
            throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_DETAILS_EXIST));
        }
    }

    public void courseDetailsWithOutIdValidation(CourseDetailsInputDto courseDetailsInputDto){
        if (courseDetailsRepository.existsByCourseName(courseDetailsInputDto.getCourseName())) {
            throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_DETAILS_EXIST));
        }
    }

    public void courseInputDetailsValidation(CourseDetailsInputDto courseDetailsInputDto) {
        courseDetailsValidation(courseDetailsInputDto);
        List<String> errorMessage = new ArrayList<>();
        courseValidation.courseValidation(courseDetailsInputDto, errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }
    }

//    public List<CourseDetailsViewDto> getAllCourseDetails() throws CustomValidationsException{
//        List<CourseDetails> courseDetails=courseDetailsRepository.findAll();
//        List<CourseDetailsViewDto> courseDetailsViewDto=new ArrayList<>();
//        if(courseDetails.isEmpty()) {
//            throw new CustomValidationsException(messageService.messageResponse("course.details.null"));
//        }
//        for(CourseDetails i: courseDetails){
//            courseDetailsViewDto.add(CourseDetailsViewDto.builder().courseId(i.getId())
//                    .courseName(i.getCourseName())
//                    .duration(i.getDuration())
//                    .description(i.getDescription())
//                    .build());
//        }
//        return courseDetailsViewDto;
//    }

    public List<CourseDetailsDto> findAllCourseDetails(){
        return courseDetailsRepository.findAllCourseDetails().orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("course.details.null")));
    }


    private void courseDetailsValidation(CourseDetailsInputDto courseDetailsInputDto) {
        if (stringCheck(courseDetailsInputDto.getCourseName())) {
            throw new CustomValidationsException(messageService.messageResponse("course.name.empty"));
        }
        if (stringCheck(courseDetailsInputDto.getDescription())) {
            throw new CustomValidationsException(messageService.messageResponse("course.description.empty"));
        }
    }

    public boolean stringCheck(String str) {
        return str == null || str.isBlank();
    }

    public CourseDetails findCourseDetailsById(UUID id) throws CustomValidationsException {
        return courseDetailsRepository.findById(id).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("course.details.empty")));
    }

    public void saveCourseDetails(CourseDetails courseDetails){
        courseDetailsRepository.save(courseDetails);
    }

//    public CourseDetailsDto findCourseDetails(String courseName){
//        return courseDetailsRepository.findCourseDetails(courseName).orElseThrow(
//                ()->new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_DETAILS_EMPTY)));
//    }


}
