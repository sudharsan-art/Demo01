package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CourseReferenceLinkRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseReferenceLinkService {
    private final CourseDetailsService courseDetailsService;

    private final CourseReferenceLinkRepository courseReferenceLinkRepository;

    private final MessageService messageService;

    private final CourseSubTopicDetailsService courseSubTopicDetailsService;

    public MessageDto addUpdateDeleteReferenceLink(CourseReferenceLinkTopicDto courseReferenceLinkTopicDto) {
        List<CourseReferenceLink> courseReferenceLinks=new ArrayList<>();
        CourseReferenceLink courseReferenceLink;
            for (CourseReferenceLinkSubTopicDto i : courseReferenceLinkTopicDto.getCourseReferenceLinkSubTopicDto()) {
               CourseSubTopicDetails courseSubTopicDetail= courseSubTopicDetailsService.findSubtopicById(i.getSubTopicId());
                if(i.getReferenceLinkId()!=null){
                    courseReferenceLink=  addCourseReferenceLinkWithId(i,courseSubTopicDetail);
                }
                else{
                    courseReferenceLink=addCourseReferenceLinkWithOutId(i,courseSubTopicDetail);
                }
                courseReferenceLinks.add(courseReferenceLink);
            }
        courseReferenceLinkRepository.saveAll(courseReferenceLinks);
        return MessageDto.builder().message(messageService.messageResponse("reference.link.added")).build();
    }

    public CourseReferenceLinkOutputDto getAllReferenceLinks(UUID courseId){
        CourseDetails courseDetails=courseDetailsService.findCourseDetailsById(courseId);
        List<CourseReferenceLinkSubTopicOutputDto> courseReferenceLinkSubTopicOutputDtoList=new ArrayList<>();
        List<CourseReferenceLinkTopicOutputDto> courseReferenceLinkTopicOutputDtoList=new ArrayList<>();
       CourseReferenceLinkSubTopicOutputDto courseReferenceLinkSubTopicOutputDto;
            for(CourseTopicDetails j:courseDetails.getCourseTopicDetails()){
                for(CourseSubTopicDetails k:j.getCourseSubTopicDetails()){
                    if(k.getCourseReferenceLinks() == null){
                        courseReferenceLinkSubTopicOutputDto=createWithOutReferenceLink(k);
                    }
                    else{
                        courseReferenceLinkSubTopicOutputDto= createWithReferenceLink(k);
                    }
                        courseReferenceLinkSubTopicOutputDtoList.add(courseReferenceLinkSubTopicOutputDto);
                }
                courseReferenceLinkTopicOutputDtoList.add(CourseReferenceLinkTopicOutputDto.builder()
                        .topicId(j.getId())
                        .topicName(j.getTopicName())
                        .courseReferenceLinkSubTopicOutputDto(courseReferenceLinkSubTopicOutputDtoList)
                        .build());
            }
       return CourseReferenceLinkOutputDto.builder()
               .courseId(courseId)
               .courseReferenceLinkTopicOutputDto(courseReferenceLinkTopicOutputDtoList)
               .build();
    }

    public CourseReferenceLinkSubTopicOutputDto createWithReferenceLink(CourseSubTopicDetails courseSubTopicDetails){
        return CourseReferenceLinkSubTopicOutputDto.builder()
                .subTopicId(courseSubTopicDetails.getId())
                .subTopicName(courseSubTopicDetails.getSubTopicName())
                .referenceId(courseSubTopicDetails.getCourseReferenceLinks().getId())
                .referenceLink(courseSubTopicDetails.getCourseReferenceLinks().getReferenceLink())
                .build();
    }
    public CourseReferenceLinkSubTopicOutputDto createWithOutReferenceLink(CourseSubTopicDetails courseSubTopicDetails){
        return CourseReferenceLinkSubTopicOutputDto.builder()
                .subTopicId(courseSubTopicDetails.getId())
                .subTopicName(courseSubTopicDetails.getSubTopicName())
                .build();
    }

    public List<EmployeeSubTopicReferenceLinkDto> findAllReferenceLinkDetails(UUID subtopicId){
        return courseReferenceLinkRepository.findAllReferenceLinkDetails(subtopicId).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("referenceLink.details.empty")));
    }





    public CourseReferenceLink addCourseReferenceLinkWithId(CourseReferenceLinkSubTopicDto courseReferenceLinkSubTopicDto,CourseSubTopicDetails courseSubTopicDetails){
        return CourseReferenceLink.builder()
                .id(courseReferenceLinkSubTopicDto.getReferenceLinkId())
                .referenceLink(courseReferenceLinkSubTopicDto.getReferenceLink())
                .status(Status.ACTIVE)
                .courseSubTopicDetails(courseSubTopicDetails)
                .build();
    }
    public CourseReferenceLink addCourseReferenceLinkWithOutId(CourseReferenceLinkSubTopicDto courseReferenceLinkSubTopicDto,CourseSubTopicDetails courseSubTopicDetails){
        return CourseReferenceLink.builder()
                .referenceLink(courseReferenceLinkSubTopicDto.getReferenceLink())
                .status(Status.ACTIVE)
                .courseSubTopicDetails(courseSubTopicDetails)
                .build();
    }




}
