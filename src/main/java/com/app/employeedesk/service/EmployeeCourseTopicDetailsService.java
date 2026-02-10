package com.app.employeedesk.service;

import com.app.employeedesk.repo.EmployeeCourseTopicDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeCourseTopicDetailsService {

    private final EmployeeCourseTopicDetailsRepository employeeCourseTopicDetailsRepository;

    public int getCompletedTopicCount(UUID enrollmentId,UUID courseId){
        return employeeCourseTopicDetailsRepository.getCompletedTopicCount(enrollmentId,courseId);
    }

    public int getCourseTopicCount(UUID enrollmentId,UUID courseId){
        return employeeCourseTopicDetailsRepository.getEmployeeTopicCount(enrollmentId,courseId);
    }


}
