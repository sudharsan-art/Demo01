package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.CourseSubTopicDetails;
import com.app.employeedesk.entity.CourseTasksDetails;
import com.app.employeedesk.entity.EmployeeCourseSubTopicDetails;
import com.app.employeedesk.entity.EmployeeCourseTaskDetails;
import com.app.employeedesk.enumeration.CourseStatus;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CourseTasksDetailsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.IConstant;
import com.app.employeedesk.util.file.FileUtils;
import com.app.employeedesk.validation.CourseMasterValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseTasksDetailsService {

    private final CourseTasksDetailsRepository courseTasksDetailsRepository;

    private final MessageService messageService;

    private final CourseMasterValidation courseMasterValidation;

    private final CourseSubTopicDetailsService courseSubTopicDetailsService;

    private final EmployeeCourseSubTopicDetailsService employeeCourseSubTopicDetailsService;

    private final EmployeeCourseTaskDetailsService employeeCourseTaskDetailsService;

    public MessageDto createTask(MultipartFile file, CourseTaskDetailsDto courseTaskDetailsDto) throws IOException, CustomValidationsException {
        courseMasterValidation.taskValidation(courseTaskDetailsDto);
        CourseSubTopicDetails courseSubTopicDetails = courseSubTopicDetailsService.findSubtopicById(courseTaskDetailsDto.getSubTopicId());
        if (courseSubTopicDetails.getCourseTasksDetails().stream().anyMatch(i -> i.getName().equals(courseTaskDetailsDto.getTaskName()))) {
            throw new CustomValidationsException(messageService.messageResponse("task.name.exist"));
        }
        courseTasksDetailsRepository.save(CourseTasksDetails.builder()
                .name(courseTaskDetailsDto.getTaskName())
                .status(Status.ACTIVE)
                .taskImage(FileUtils.compressFile(file.getBytes()))
                .courseSubTopicDetails(courseSubTopicDetails)
                .build());
        return MessageDto.builder().message(messageService.messageResponse("task.create.success")).build();
    }

    public MessageDto deleteTask(UUID taskId) {
        courseTasksDetailsRepository.deleteById(taskId);
        return MessageDto.builder().message(messageService.messageResponse("task.delete.success")).build();
    }

    public MessageDto updateTask(MultipartFile file, CourseTasksDetailsOutputDto courseTasksDetailsOutputDto) throws IOException, CustomValidationsException {
        courseMasterValidation.taskValidation(courseTasksDetailsOutputDto);
        if(courseTasksDetailsRepository.existsById(courseTasksDetailsOutputDto.getTaskId())){
            throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_TASK_DETAILS_EMPTY));
        }
        CourseSubTopicDetails courseSubTopicDetails = courseSubTopicDetailsService.findSubtopicById(courseTasksDetailsOutputDto.getSubTopicId());
        if (courseSubTopicDetails.getCourseTasksDetails().stream().anyMatch(i -> i.getName().equals(courseTasksDetailsOutputDto.getTaskName())
                && !i.getId().equals(courseTasksDetailsOutputDto.getTaskId()))) {
            throw new CustomValidationsException(messageService.messageResponse("task.name.exist"));
        }
        courseTasksDetailsRepository.save(CourseTasksDetails.builder()
                .id(courseTasksDetailsOutputDto.getTaskId())
                .name(courseTasksDetailsOutputDto.getTaskName())
                .status(Status.ACTIVE)
                .taskImage(FileUtils.compressFile(file.getBytes()))
                .courseSubTopicDetails(courseSubTopicDetails)
                .build());
        return MessageDto.builder().message(messageService.messageResponse("task.update.success")).build();
    }

    public List<EmployeeCourseTaskDetailsViewDto> getAllEmployeeTaskDetails(UUID subTopicId) throws CustomValidationsException {
        List<EmployeeCourseTaskDetailsViewDto> employeeCourseTaskDetailsViewDto=courseTasksDetailsRepository.findEmployeeAllTaskDetails(subTopicId);
        if(employeeCourseTaskDetailsViewDto.isEmpty()){
        throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_TASK_DETAILS_EMPTY));
        }
        for(EmployeeCourseTaskDetailsViewDto i: employeeCourseTaskDetailsViewDto){
            if(i.getTaskStatus()==null){
                i.setTaskStatus(CourseStatus.INCOMPLETE);
            }
        }
        return employeeCourseTaskDetailsViewDto;
    }

    public MessageDto submitEmployeeCourseTask(MultipartFile file, TaskSubmitInputDto taskSubmitInputDto) throws IOException {
        courseMasterValidation.taskValidation(taskSubmitInputDto);
        EmployeeCourseTaskDetails employeeCourseTaskDetails=employeeCourseTaskDetailsService.getEmployeeTaskDetails(taskSubmitInputDto.getTaskId(),taskSubmitInputDto.getEmployeeSubTopicId());
        employeeCourseTaskDetails.setStatus(CourseStatus.COMPLETED);
        employeeCourseTaskDetails.setTaskUrl(taskSubmitInputDto.getUrl());
        employeeCourseTaskDetails.setSnapshot(FileUtils.compressFile(file.getBytes()));
        employeeCourseTaskDetails.setSubmittedOn(LocalDate.now());
        employeeCourseTaskDetails.setDuration(findTaskDuration(employeeCourseTaskDetails.getStartTime()));
        return employeeCourseTaskDetailsService.submitTask(employeeCourseTaskDetails);
    }
    private double findTaskDuration(LocalTime startTime){
        LocalTime endTime=LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return hours + (minutes / 60.0);
    }


    public MessageDto taskStart(TaskStartInputDto taskStartInputDto){
        CourseTasksDetails courseTasksDetails= courseTasksDetailsRepository.findById(taskStartInputDto.getTaskId()).orElseThrow(
                ()-> new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_TASK_DETAILS_EMPTY)));
        EmployeeCourseSubTopicDetails employeeCourseSubTopicDetails=employeeCourseSubTopicDetailsService.findById(taskStartInputDto.getEmployeeSubTopicId());
        employeeCourseTaskDetailsService.buildEmployeeCourseTaskDetails(courseTasksDetails,employeeCourseSubTopicDetails);
        return MessageDto.builder().message(messageService.messageResponse("task.start.successful")).build();
    }

}
