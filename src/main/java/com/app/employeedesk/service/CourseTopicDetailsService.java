package com.app.employeedesk.service;

import com.app.employeedesk.config.AppProperties;
import com.app.employeedesk.dto.*;
import com.app.employeedesk.dto.MessageDto;
import com.app.employeedesk.entity.CourseDetails;
import com.app.employeedesk.entity.CourseSubTopicDetails;
import com.app.employeedesk.entity.CourseSubTopicWeekDurationDetails;
import com.app.employeedesk.entity.CourseTopicDetails;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.CourseMasterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class CourseTopicDetailsService {

    private final MessageService messageService;

    private final CourseSubTopicDetailsService courseSubTopicDetailsService;

    private final CourseMasterValidation courseValidation;

    private final CourseDetailsService courseDetailsService;

    private final AppProperties appProperties;

    //This method is used to get course details based on course id
    public CourseDetailsInputViewDto getCourseAllDetails(UUID courseId) throws CustomValidationsException {
        CourseDetails courseDetails = courseDetailsService.findCourseDetailsById(courseId);
        List<CourseTopicDetailsViewDto> topicSubTopicOutputDto = new ArrayList<>();
        if(courseDetails.getCourseTopicDetails()!=null) {
            for (CourseTopicDetails j : courseDetails.getCourseTopicDetails()) {
                List<SubTopicOutputDto> subTopicOutputDto = new ArrayList<>();
                if(j.getCourseSubTopicDetails()!=null) {
                    for (CourseSubTopicDetails k : j.getCourseSubTopicDetails()) {
                        subTopicOutputDto.add(courseSubTopicDetailsService.createSubTopicDetails(k));
                    }
                }
                topicSubTopicOutputDto.add(createTopicDetails(j, subTopicOutputDto));
            }
        }
        return CourseDetailsInputViewDto.builder()
                .courseId(courseDetails.getId())
                .courseName(courseDetails.getCourseName())
                .description(courseDetails.getDescription())
                .duration(courseDetails.getDuration())
                .courseTopicDetailsViewDto(topicSubTopicOutputDto)
                .build();
    }

//This method is used to add delete update course details and automatically allocate weeks for each sub topic
    public MessageDto addUpdateDeleteCourseTopicSubTopicDetails(CourseDetailsInputDto courseDetailsInputDto) {
        CourseDetails courseDetails = updateCourseDetails(courseDetailsInputDto);
        List<CourseTopicDetails> topicList = new ArrayList<>();
        CourseTopicDetails courseTopicDetails;
        CourseSubTopicDetails courseSubTopicDetails;
        double weekBalance = appProperties.getWeekDays();
        int weekNumber = appProperties.getWeekNumber();
        Set<String> topicSet = new HashSet<>();
        double topicDuration = appProperties.getTopicDuration();
        if(courseDetailsInputDto.getTopicDetailsInputDto()!=null) {
            for (TopicOutputDto topicInputDetails : courseDetailsInputDto.getTopicDetailsInputDto()) {
                courseValidation.topicDetailsValidation(topicInputDetails);
                topicListDuplicateCheck(topicSet, topicInputDetails.getTopicName());
                courseTopicDetails = updateTopicDetails(courseDetails, topicInputDetails);
                List<CourseSubTopicDetails> subTopicList = new ArrayList<>();
                double subtopicDuration = appProperties.getSubTopicDuration();
                Set<String> subTopicSet = new HashSet<>();
                if(topicInputDetails.getSubTopicDetailsInputDto()!=null) {
                    for (SubTopicOutputDto subTopicInputDetails : topicInputDetails.getSubTopicDetailsInputDto()) {
                        courseValidation.subTopicDetailsValidation(subTopicInputDetails);
                        subTopicListDuplicateCheck(subTopicSet, subTopicInputDetails.getSubTopicName());
                        courseSubTopicDetails = updateSubTopicDetails(subTopicInputDetails, courseTopicDetails, courseDetails);
                        subtopicDuration += subTopicInputDetails.getDuration();
                        List<CourseSubTopicWeekDurationDetails> courseSubTopicWeekDurationDetailsList = new ArrayList<>();
                        List<Integer> assignedWeeks = getWeeksForSubTopic(subTopicInputDetails.getDuration(), weekNumber, weekBalance);
                        courseSubTopicWeekDurationDetailsList = buildWeekDurationDetails(assignedWeeks, courseSubTopicDetails, courseSubTopicWeekDurationDetailsList);
                        weekNumber = assignedWeeks.get(assignedWeeks.size() - 1);
                        weekBalance = updateWeekBalance(weekBalance, subTopicInputDetails.getDuration());
                        courseSubTopicDetails.setSubTopicWeekDurationDetails(courseSubTopicWeekDurationDetailsList);
                        subTopicList.add(courseSubTopicDetails);
                    }
                }
                courseTopicDetails.setCourseSubTopicDetails(subTopicList);
                courseTopicDetails.setDuration(subtopicDuration);
                topicDuration += subtopicDuration;
                topicList.add(courseTopicDetails);
            }
        }
        courseDetails.setDuration(topicDuration);

        courseDetails.setCourseTopicDetails(topicList);
        courseDetails.setTotalWeek(weekNumber);
        courseDetailsService.saveCourseDetails(courseDetails);
        return MessageDto.builder().message(messageService.messageResponse("course.topic.subtopic.update")).build();
    }

    //This method is used to find balance days from the week
    public double updateWeekBalance(double weekBalance, double subtopicDuration) {
        double days = subtopicDuration / 8.0;
        weekBalance -= days;
        return weekBalance;
    }

    //This method is used to find how many weeks that the duration occupy
    public List<Integer> getWeeksForSubTopic(double subtopicDuration, int weekNumber, double weekBalance) {
        List<Integer> assignedWeeks = new ArrayList<>();
        double days = subtopicDuration / 8.0;
        while (days > 0 ) {
            if (weekBalance >0) {
                assignedWeeks.add(weekNumber);
                days -=weekBalance;
                weekBalance=0;
            } else {
                weekBalance = 5.0;
                weekNumber++;
                assignedWeeks.add(weekNumber);
                days -=weekBalance;
                weekBalance=0;
            }
        }
        return assignedWeeks;
    }


//This method is used to build week details of the sub topics
    public List<CourseSubTopicWeekDurationDetails> buildWeekDurationDetails(List<Integer> assignedWeeks, CourseSubTopicDetails courseSubTopicDetails, List<CourseSubTopicWeekDurationDetails> courseSubTopicWeekDurationDetailsList){
        for (Integer week : assignedWeeks) {
            courseSubTopicWeekDurationDetailsList.add(CourseSubTopicWeekDurationDetails.builder()
                    .courseSubTopicDetails(courseSubTopicDetails)
                    .weekNumber(week)
                    .status(Status.ACTIVE)
                    .build());
        }
        return courseSubTopicWeekDurationDetailsList;
    }

    //This method is used to update the course details
    public CourseDetails updateCourseDetails(CourseDetailsInputDto courseDetailsInputDto) {
        courseDetailsService.courseInputDetailsValidation(courseDetailsInputDto);
        if (courseDetailsInputDto.getCourseId() != null) {
            courseDetailsService.courseDetailsWithIdValidation(courseDetailsInputDto);
            CourseDetails courseDetails=courseDetailsService.buildCourseDetails(courseDetailsInputDto);
            courseDetails.setId(UUID.fromString(courseDetailsInputDto.getCourseId()));
            return courseDetails;
        } else {
            courseDetailsService.courseDetailsWithOutIdValidation(courseDetailsInputDto);
            return courseDetailsService.buildCourseDetails(courseDetailsInputDto);
        }
    }

    //This method is used to update the topic details
    public CourseTopicDetails updateTopicDetails(CourseDetails courseDetails, TopicOutputDto topicSubTopicOutputDto) {
        if (topicSubTopicOutputDto.getTopicId() != null) {
            CourseTopicDetails courseTopicDetails=buildTopicDetails(courseDetails,topicSubTopicOutputDto);
            courseTopicDetails.setId(topicSubTopicOutputDto.getTopicId());
            return courseTopicDetails;
        } else {
            return buildTopicDetails(courseDetails,topicSubTopicOutputDto);
        }
    }

    //This method is used to update the sub topic details
    public CourseSubTopicDetails updateSubTopicDetails(SubTopicOutputDto subTopicOutputDto, CourseTopicDetails courseTopicDetails,CourseDetails courseDetails) {
        if (subTopicOutputDto.getSubTopicId() != null) {
            CourseSubTopicDetails courseSubTopicDetails=courseSubTopicDetailsService.buildSubTopic(subTopicOutputDto,courseTopicDetails,courseDetails);
            courseSubTopicDetails.setId(subTopicOutputDto.getSubTopicId());
           return courseSubTopicDetails;
        } else {
            return courseSubTopicDetailsService.buildSubTopic(subTopicOutputDto,courseTopicDetails,courseDetails);
        }
    }

//This method is used to build the topic details object
    public CourseTopicDetails buildTopicDetails(CourseDetails courseDetails, TopicOutputDto topicSubTopicOutputDto){
        return CourseTopicDetails.builder()
                .topicName(topicSubTopicOutputDto.getTopicName())
                .description(topicSubTopicOutputDto.getDescription())
                .status(Status.ACTIVE)
                .courseDetails(courseDetails)
                .build();
    }

    public void topicListDuplicateCheck(Set<String> topicSet, String topicName) throws CustomValidationsException {
        if (!topicSet.add(topicName)) {
            throw new CustomValidationsException(messageService.messageResponse("topicList.duplicate.contains"));
        }
    }
    public void subTopicListDuplicateCheck(Set<String> subTopicSet, String subTopicName) throws CustomValidationsException {
        if (!subTopicSet.add(subTopicName)) {
            throw new CustomValidationsException(messageService.messageResponse("subtopicList.duplicate.contains"));
        }
    }

    public CourseTopicDetailsViewDto createTopicDetails(CourseTopicDetails courseTopicDetails, List<SubTopicOutputDto> subTopicOutputDto) {
        return CourseTopicDetailsViewDto.builder()
                .topicId(courseTopicDetails.getId())
                .topicName(courseTopicDetails.getTopicName())
                .description(courseTopicDetails.getDescription())
                .subTopicDetailsInputDto(subTopicOutputDto)
                .build();
    }


}
