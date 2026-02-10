package com.app.employeedesk.service;

import com.app.employeedesk.config.AppProperties;
import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.CourseStatus;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeeCourseEnrollmentRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.util.IConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeCourseEnrollmentService {

    private final EmployeeCourseEnrollmentRepository employeeCourseEnrollmentRepository;

    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    private final CourseDetailsService courseDetailsService;

    private final MessageService messageService;

    private final EmployeeCourseTopicDetailsService employeeCourseTopicDetailsService;

    private final AppProperties appProperties;


    //This method is used to delete the course enrollment details
    public MessageDto deleteEmployeeCourseEnrollment(String courseEnrollmentId) {
        employeeCourseEnrollmentRepository.deleteById(UUID.fromString(courseEnrollmentId));
        return MessageDto.builder()
                .message(messageService.messageResponse("courseEnrollment.details.delete"))
                .build();
    }

    //This method is used to update the employee course status
    public MessageDto markAsCompleted(String courseEnrollmentId, String remarks) {
        EmployeeCourseEnrollmentDetails employeeCourseEnrollmentDetails = findById(UUID.fromString(courseEnrollmentId));
        employeeCourseEnrollmentDetails.setCourseStatus(CourseStatus.COMPLETED);
        employeeCourseEnrollmentDetails.setRemarks(remarks);
        employeeCourseEnrollmentRepository.save(employeeCourseEnrollmentDetails);
        return MessageDto.builder().message(messageService.messageResponse("course.mark.complete")).build();
    }

    //This method is used to get employee course enrollment progress list
    public List<CourseEnrollmentProgressDto> courseEnrollmentProgressList(String courseId) {
        List<CourseEnrollmentProgressDto> courseEnrollmentProgressDto = employeeCourseEnrollmentRepository.getAllCourseEnrollmentProgress(UUID.fromString(courseId));
        if (courseEnrollmentProgressDto.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_ENROLLMENT_DETAILS_EMPTY));
        }
        return courseEnrollmentProgressDto;
    }

    //This method is used to get employee course enrollment completed list
    public List<CourseEnrollmentCompletedDto> courseEnrollmentCompletedList(String courseId) {
        List<CourseEnrollmentCompletedDto> courseEnrollmentCompletedDto = employeeCourseEnrollmentRepository.getAllCourseEnrollmentComplete(UUID.fromString(courseId));
        if (courseEnrollmentCompletedDto.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_ENROLLMENT_DETAILS_EMPTY));
        }
        return courseEnrollmentCompletedDto;
    }

    //This method is used to save the employee course enrollment details
    public MessageDto employeeCourseEnrollment(CourseEnrollmentDto courseEnrollmentDto) {
        inputValidation(courseEnrollmentDto);
        EmployeeBasicDetails employeeBasicDetails = employeePersonalDetailsService.findEmployeeById(UUID.fromString(courseEnrollmentDto.getEmployeeId()));
        CourseDetails courseDetails = courseDetailsService.findCourseDetailsById(UUID.fromString(courseEnrollmentDto.getCourseId()));
        EmployeeCourseEnrollmentDetails employeeCourseEnrollmentDetails = EmployeeCourseEnrollmentDetails.builder()
                .courseDetails(courseDetails)
                .employeeBasicDetails(employeeBasicDetails)
                .courseStatus(CourseStatus.PROGRESS)
                .totalWeeksCompleted(IConstant.ZERO)
                .joiningDate(DateUtil.parseLocalDate(courseEnrollmentDto.getJoiningDate()))
                .startDate(DateUtil.parseLocalDate(courseEnrollmentDto.getJoiningDate()))
                .endDate(findEndDate(courseEnrollmentDto.getJoiningDate(), courseDetails.getDuration()))
                .build();
        employeeCourseEnrollmentRepository.save(employeeCourseEnrollmentDetails);
        return MessageDto.builder().message(messageService.messageResponse("course.enrolled.success")).build();
    }


    private LocalDate findEndDate(String startDate, Double duration) {
        return Objects.requireNonNull(DateUtil.parseLocalDate(startDate))
                .plusDays((long) Math.ceil(duration / appProperties.getDayHours()));
    }

    //This method is used to get all employee enrolled courses
    public List<CourseDetailsViewDto> getEmployeeAllEnrolledCourses(String employeeId) throws CustomValidationsException {
        List<CourseDetailsViewDto> courseDetailsViewDto = employeeCourseEnrollmentRepository.getEmployeeAllEnrolledCourseDetails(UUID.fromString(employeeId));
        if (courseDetailsViewDto.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse("employee.enrolled.course.empty"));
        }
        int totalTopics;
        int completedTopics;
        for (CourseDetailsViewDto courseDetails : courseDetailsViewDto) {
            totalTopics = employeeCourseTopicDetailsService.getCourseTopicCount(UUID.fromString(courseDetails.getCourseEnrollmentId()), UUID.fromString(courseDetails.getCourseId()));
            completedTopics = employeeCourseTopicDetailsService.getCompletedTopicCount(UUID.fromString(courseDetails.getCourseEnrollmentId()), UUID.fromString(courseDetails.getCourseId()));
            courseDetails.setTopicsDetails(totalTopics, completedTopics);
        }
        return courseDetailsViewDto;
    }

    private EmployeeCourseEnrollmentDetails findById(UUID courseEnrollmentId) throws CustomValidationsException {
        return employeeCourseEnrollmentRepository.findById(courseEnrollmentId).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_ENROLLMENT_DETAILS_EMPTY)));
    }


    public CourseEnrollmentProgressDetailsDto getEmployeeCourseProgressDetails(String enrollmentId) {
        Map<String, List<CourseEnrollmentProgressTopicDto>> employeeCourseProgressList = new HashMap<>();
        List<CourseEnrollmentProgressTopicDto> inProgress = new ArrayList<>();
        List<CourseEnrollmentProgressTopicDto> completed = new ArrayList<>();
        EmployeeCourseEnrollmentDetails courseEnrollmentDetails = getEmployeeCourseDetailsById(enrollmentId);
        CourseDetails courseDetails = courseDetailsService.findCourseDetailsById(courseEnrollmentDetails.getCourseDetails().getId());
        Integer completedWeeksCount = addCompletedWeeks(courseEnrollmentDetails.getJoiningDate());
        if(completedWeeksCount>=courseDetails.getTotalWeek()) {
            courseEnrollmentDetails.setTotalWeeksCompleted(courseDetails.getTotalWeek());
            if (findCourseStatus(courseEnrollmentDetails, courseDetails.getTotalWeek(), courseDetails))
                courseEnrollmentDetails.setCourseStatus(CourseStatus.COMPLETED);
            else
                courseEnrollmentDetails.setCourseStatus(CourseStatus.INCOMPLETE);
        }
        employeeCourseEnrollmentRepository.save(courseEnrollmentDetails);
        for (CourseTopicDetails courseTopicDetails : courseDetails.getCourseTopicDetails()) {
            List<CourseEnrollmentProgressSubTopicDto> courseEnrollmentProgressSubTopicDtoList = new ArrayList<>();
            for (CourseSubTopicDetails courseSubTopicDetails : courseTopicDetails.getCourseSubTopicDetails()) {
                Integer weekNumber = findWeekNumber(courseSubTopicDetails);
                courseEnrollmentProgressSubTopicDtoList.add(buildCourseEnrollmentProgressSubTopicDto(courseSubTopicDetails, weekNumber, courseEnrollmentDetails));
            }
            CourseEnrollmentProgressTopicDto courseEnrollmentProgressTopicDto = buildCourseEnrollmentProgressTopicDto(courseTopicDetails, courseEnrollmentProgressSubTopicDtoList);
            if (findTopicStatus(courseEnrollmentDetails,courseEnrollmentProgressTopicDto))
                completed.add(courseEnrollmentProgressTopicDto);
             else
                inProgress.add(courseEnrollmentProgressTopicDto);
        }
        employeeCourseProgressList.put("InProgress", inProgress);
        employeeCourseProgressList.put("Completed", completed);
        return CourseEnrollmentProgressDetailsDto.builder()
                .courseName(courseDetails.getCourseName())
                .progressList(employeeCourseProgressList)
                .build();
    }

    private boolean findTopicStatus(EmployeeCourseEnrollmentDetails courseEnrollmentDetails,CourseEnrollmentProgressTopicDto courseEnrollmentProgressTopicDto){
        List<UUID> subTopicId=courseEnrollmentDetails.getEmployeeCourseTopicDetails().stream()
                .filter(f->f.getId()!=null && f.getId().equals(courseEnrollmentProgressTopicDto.getTopicId()))
                .flatMap(o->o.getEmployeeCourseSubTopicDetails().stream())
                .map(EmployeeCourseSubTopicDetails::getId).toList();
        return courseEnrollmentProgressTopicDto.getCourseEnrollmentProgressSubTopic().stream()
                .allMatch(k->k.getSubTopicId()!=null && subTopicId.contains(k.getSubTopicId()));
    }

    private EmployeeCourseEnrollmentDetails getEmployeeCourseDetailsById(String enrollmentId) {
        return employeeCourseEnrollmentRepository.findById(UUID.fromString(enrollmentId)).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse(IConstant.COURSE_ENROLLMENT_DETAILS_EMPTY)));
    }

    private List<UUID> findFinalWeekSubTopicId(CourseDetails courseDetails, Integer totalWeek) {
        return courseDetails.getCourseTopicDetails().stream()
                .flatMap(o -> o.getCourseSubTopicDetails().stream())
                .flatMap(k -> k.getSubTopicWeekDurationDetails().stream()
                        .filter(a -> a != null && a.getWeekNumber().equals(totalWeek)))
                .map(CourseSubTopicWeekDurationDetails::getCourseSubTopicDetails).map(CourseSubTopicDetails::getId)
                .toList();
    }

    private boolean findCourseStatus(EmployeeCourseEnrollmentDetails courseEnrollmentDetails, Integer totalWeek, CourseDetails courseDetails) {
        List<UUID> subTopicList = findFinalWeekSubTopicId(courseDetails, totalWeek);
        List<UUID> enrollmentSubTopicList=new ArrayList<>(courseEnrollmentDetails.getEmployeeCourseTopicDetails().stream()
                .flatMap(o -> o.getEmployeeCourseSubTopicDetails().stream())
                .map(EmployeeCourseSubTopicDetails::getId).toList());
        if(enrollmentSubTopicList.isEmpty()){
            enrollmentSubTopicList.add(null);
        }
        return new ArrayList<>(subTopicList).containsAll(enrollmentSubTopicList);
    }

    private Integer addCompletedWeeks(LocalDate joiningDate) {
        LocalDate currentDate = LocalDate.now();
        long totalDays = ChronoUnit.DAYS.between(joiningDate, currentDate);
        int weeksCompleted = (int) Math.ceil(totalDays / appProperties.getWeekDays());
        return weeksCompleted > IConstant.ZERO ? weeksCompleted - IConstant.ONE : IConstant.ZERO;
    }

    private Integer findWeekNumber(CourseSubTopicDetails courseSubTopicDetails) {
        return courseSubTopicDetails.getSubTopicWeekDurationDetails().stream()
                .filter(w -> w.getCourseSubTopicDetails().getId().equals(courseSubTopicDetails.getId()))
                .map(CourseSubTopicWeekDurationDetails::getWeekNumber)
                .findFirst().orElse(null);
    }

    private CourseEnrollmentProgressSubTopicDto buildCourseEnrollmentProgressSubTopicDto(CourseSubTopicDetails courseSubTopicDetails, Integer weekNumber, EmployeeCourseEnrollmentDetails courseEnrollmentDetails) {
        CourseEnrollmentProgressSubTopicDto courseEnrollmentProgressSubTopicDto = CourseEnrollmentProgressSubTopicDto.builder()
                .subTopicId(courseSubTopicDetails.getId())
                .subTopicName(courseSubTopicDetails.getSubTopicName())
                .weekNumber(weekNumber)
                .build();
        CourseStatus subTopicStatus = findSubTopicStatus(courseEnrollmentDetails, courseSubTopicDetails);
        if (subTopicStatus == null) {
            courseEnrollmentProgressSubTopicDto.setStatus(CourseStatus.PROGRESS);
        } else {
            courseEnrollmentProgressSubTopicDto.setStatus(CourseStatus.COMPLETED);
        }
        return courseEnrollmentProgressSubTopicDto;
    }

    private CourseStatus findSubTopicStatus(EmployeeCourseEnrollmentDetails courseEnrollmentDetails, CourseSubTopicDetails courseSubTopicDetails) {
        return courseEnrollmentDetails.getEmployeeCourseTopicDetails().stream()
                .flatMap(o -> o.getEmployeeCourseSubTopicDetails().stream())
                .filter(k -> k.getId().equals(courseSubTopicDetails.getId()))
                .map(EmployeeCourseSubTopicDetails::getStatus)
                .findFirst().orElse(null);
    }

    private CourseEnrollmentProgressTopicDto buildCourseEnrollmentProgressTopicDto(CourseTopicDetails courseTopicDetails, List<CourseEnrollmentProgressSubTopicDto> courseEnrollmentProgressSubTopicDtoList) {
        return CourseEnrollmentProgressTopicDto.builder()
                .topicId(courseTopicDetails.getId())
                .topicName(courseTopicDetails.getTopicName())
                .courseEnrollmentProgressSubTopic(courseEnrollmentProgressSubTopicDtoList)
                .build();
    }

    private void inputValidation(CourseEnrollmentDto courseEnrollmentDto) {
        if (stringCheck(courseEnrollmentDto.getCourseId())) {
            throw new CustomValidationsException(messageService.messageResponse("courseEnrollment.id.empty"));
        }
        if (stringCheck(courseEnrollmentDto.getEmployeeId())) {
            throw new CustomValidationsException(messageService.messageResponse("employee.id.empty"));
        }
        if (stringCheck(courseEnrollmentDto.getJoiningDate())) {
            throw new CustomValidationsException(messageService.messageResponse("courseEnrollment.date.empty"));
        }
        if (!DateUtil.isValidLocalDate(courseEnrollmentDto.getJoiningDate())) {
            throw new CustomValidationsException(messageService.messageResponse("courseEnrollment.date.invalid"));
        }
    }

    public boolean stringCheck(String str) {
        return str == null || str.isBlank();
    }
}
