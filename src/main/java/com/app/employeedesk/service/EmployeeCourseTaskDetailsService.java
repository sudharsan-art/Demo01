package com.app.employeedesk.service;

import com.app.employeedesk.dto.MessageDto;
import com.app.employeedesk.entity.CourseTasksDetails;
import com.app.employeedesk.entity.EmployeeCourseSubTopicDetails;
import com.app.employeedesk.entity.EmployeeCourseTaskDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeeCourseTaskDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeCourseTaskDetailsService {

    private final EmployeeCourseTaskDetailsRepository employeeCourseTaskDetailsRepository;

    private final MessageService messageService;


    public void buildEmployeeCourseTaskDetails(CourseTasksDetails courseTasksDetails,EmployeeCourseSubTopicDetails employeeCourseSubTopicDetails){
        employeeCourseTaskDetailsRepository.save(EmployeeCourseTaskDetails.builder()
                .startTime(LocalTime.now())
                .employeeCourseSubTopicDetails(employeeCourseSubTopicDetails)
                .courseTasksDetails(courseTasksDetails)
                .build());
    }

    public EmployeeCourseTaskDetails getEmployeeTaskDetails(UUID taskId,UUID employeeSubTopicId){
        return employeeCourseTaskDetailsRepository.getEmployeeTaskDetails(taskId,employeeSubTopicId).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("task.details.empty")));
    }

    public MessageDto submitTask(EmployeeCourseTaskDetails employeeCourseTaskDetails){
        employeeCourseTaskDetailsRepository.save(employeeCourseTaskDetails);
        return MessageDto.builder().message(messageService.messageResponse("task.submit.successfully")).build();
    }

}
