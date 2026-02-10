package com.app.employeedesk.validation;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CourseMasterValidation {

    private final MessageService messageService;

    Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!]");

    public void courseValidation(CourseDetailsInputDto courseDetailsInputDto, List<String> errorMessage) {
        if (courseDetailsInputDto.getCourseName().length() > 50
                || courseDetailsInputDto.getDescription().length() > 255
        ) {
            errorMessage.add(messageService.messageResponse("course.input.large"));
        }
        if (regex.matcher(courseDetailsInputDto.getCourseName()).find()) {
            errorMessage.add(messageService.messageResponse("course.input.symbols"));
        }
    }


    public void topicDetailsValidation(TopicOutputDto topicSubTopicOutputDto)throws CustomValidationsException {
        if (stringCheck(topicSubTopicOutputDto.getTopicName())
                || stringCheck(topicSubTopicOutputDto.getDescription())) {
            throw new CustomValidationsException(messageService.messageResponse("topic.inputs.empty"));
        }
        if (topicSubTopicOutputDto.getTopicName().length() > 50 || topicSubTopicOutputDto.getDescription().length() > 255) {
            throw new CustomValidationsException(messageService.messageResponse("topic.inputs.large"));
        }
        if (regex.matcher(topicSubTopicOutputDto.getTopicName()).find() || regex.matcher(topicSubTopicOutputDto.getDescription()).find()) {
            throw new CustomValidationsException(messageService.messageResponse("topic.inputs.symbols"));
        }
    }

    public void subTopicDetailsValidation(SubTopicOutputDto subTopicOutputDto)throws CustomValidationsException {
        if (stringCheck(subTopicOutputDto.getSubTopicName())
                || stringCheck(subTopicOutputDto.getDescription())
        || stringCheck(subTopicOutputDto.getDuration().toString())) {
            throw new CustomValidationsException(messageService.messageResponse("subtopic.inputs.empty"));
        }
        if (subTopicOutputDto.getSubTopicName().length() > 50 || subTopicOutputDto.getDescription().length() > 255) {
            throw new CustomValidationsException(messageService.messageResponse("subtopic.inputs.large"));
        }
        if (regex.matcher(subTopicOutputDto.getSubTopicName()).find() || regex.matcher(subTopicOutputDto.getDescription()).find()) {
            throw new CustomValidationsException( messageService.messageResponse("subtopic.inputs.symbols"));
        }
        if(subTopicOutputDto.getDuration()==0||subTopicOutputDto.getDuration()<=-1 || subTopicOutputDto.getDuration()>=480){
            throw new CustomValidationsException( messageService.messageResponse("subtopic.duration.invalid"));
        }
    }

    public void materialValidation(CourseMaterialsDto courseMaterialsDto) {
        if (stringCheck(courseMaterialsDto.getName())) {
            throw new CustomValidationsException(messageService.messageResponse("material.name.empty"));
        }
        if (stringCheck(courseMaterialsDto.getType())) {
            throw new CustomValidationsException(messageService.messageResponse("material.type.empty"));
        }
        if (stringCheck(courseMaterialsDto.getDescription())) {
            throw new CustomValidationsException(messageService.messageResponse("material.description.empty"));
        }
        if (courseMaterialsDto.getName().length() > 50
                || courseMaterialsDto.getDescription().length() > 255) {
           throw new CustomValidationsException(messageService.messageResponse("material.inputs.large"));
        }
        if (regex.matcher(courseMaterialsDto.getName()).find()
                ||regex.matcher(courseMaterialsDto.getDescription()).find()) {
          throw new CustomValidationsException(messageService.messageResponse("material.inputs.symbols"));
        }
    }
    public void taskValidation(CourseTaskDetailsDto courseTaskDetailsDto)throws CustomValidationsException{
        if (stringCheck(courseTaskDetailsDto.getTaskName())) {
            throw new CustomValidationsException(messageService.messageResponse("task.name.empty"));
        }
        if (courseTaskDetailsDto.getTaskName().length() > 50){
            throw new CustomValidationsException(messageService.messageResponse("task.name.length"));
        }
        if (regex.matcher(courseTaskDetailsDto.getTaskName()).find()){
            throw new CustomValidationsException(messageService.messageResponse("task.name.symbol"));
        }
    }
    public void taskValidation(CourseTasksDetailsOutputDto courseTasksDetailsOutputDto) {

        if (courseTasksDetailsOutputDto.getTaskName().length() > 50){
            throw new CustomValidationsException(messageService.messageResponse("task.name.length"));
        }
        if (regex.matcher(courseTasksDetailsOutputDto.getTaskName()).find()){
            throw new CustomValidationsException(messageService.messageResponse("task.name.symbol"));
        }
    }

    public void taskValidation(TaskSubmitInputDto taskSubmitInputDto){
        if(stringCheck(taskSubmitInputDto.getTaskId().toString())){
            throw new CustomValidationsException(messageService.messageResponse("task.id.empty"));
        }
        if(stringCheck(taskSubmitInputDto.getUrl())){
            throw new CustomValidationsException(messageService.messageResponse("task.url.empty"));
        }
    }

    public boolean stringCheck(String str) {
        return str == null || str.isBlank();
    }


}

