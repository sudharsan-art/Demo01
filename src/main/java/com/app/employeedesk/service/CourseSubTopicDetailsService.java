package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.CourseDetails;
import com.app.employeedesk.entity.CourseSubTopicDetails;
import com.app.employeedesk.entity.CourseTopicDetails;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CourseSubTopicDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseSubTopicDetailsService {

    private final CourseSubTopicDetailsRepository courseSubTopicDetailsRepository;

    private final MessageService messageService;

    /*This method is used to build new subtopic objects */
    public SubTopicOutputDto createSubTopicDetails(CourseSubTopicDetails courseSubTopicDetails) {
        return SubTopicOutputDto.builder()
                .subTopicId(courseSubTopicDetails.getId())
                .subTopicName(courseSubTopicDetails.getSubTopicName())
                .description(courseSubTopicDetails.getDescription())
                .duration(courseSubTopicDetails.getDuration())
                .build();
    }


    public CourseSubTopicDetails buildSubTopic(SubTopicOutputDto subTopicOutputDto, CourseTopicDetails courseTopicDetails, CourseDetails courseDetails){
        return CourseSubTopicDetails.builder()
                .subTopicName(subTopicOutputDto.getSubTopicName())
                .description(subTopicOutputDto.getDescription())
                .duration(subTopicOutputDto.getDuration())
                .status(Status.ACTIVE)
                .courseTopicDetails(courseTopicDetails)
                .build();
    }

    public void saveSubTopics(List<CourseSubTopicDetails> courseSubTopicDetails){
        courseSubTopicDetailsRepository.saveAll(courseSubTopicDetails);
    }

    public CourseSubTopicDetails findSubtopicById(UUID subTopicId) throws CustomValidationsException{
        return courseSubTopicDetailsRepository.findById(subTopicId).orElseThrow(
                ()->new CustomValidationsException(new CustomValidationsException(messageService.messageResponse(""))));
    }

}
