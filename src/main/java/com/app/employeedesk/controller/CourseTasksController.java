package com.app.employeedesk.controller;

import com.app.employeedesk.dto.CourseTaskDetailsDto;
import com.app.employeedesk.dto.CourseTasksDetailsOutputDto;
import com.app.employeedesk.dto.TaskStartInputDto;
import com.app.employeedesk.dto.TaskSubmitInputDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CourseTasksDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class CourseTasksController {

    private final CourseTasksDetailsService courseTasksDetailsService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;
    @PostMapping("/create")
    public ResponseEntity<Response> createTask(@RequestPart("file") MultipartFile multipartFile, @RequestPart("data") CourseTaskDetailsDto courseTaskDetailsDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("create new task {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.createTask(multipartFile, courseTaskDetailsDto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while task details are wrong ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            logger.error("error occurred while upload task file ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteTask(@RequestBody UUID taskId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("task deleted {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.deleteTask(taskId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while delete task  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

        @GetMapping("/getAllTasksDetails/{subTopicId}")
        public ResponseEntity<Response> getAllEmployeeTasksDetails(@PathVariable String subTopicId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all task details{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.getAllEmployeeTaskDetails(UUID.fromString(subTopicId)), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while no task found ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Response> startEmployeeTasks(@RequestBody TaskStartInputDto taskStartInputDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("start task details{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.taskStart(taskStartInputDto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while no details found ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/submit")
    public ResponseEntity<Response> submitEmployeeTasks(@RequestPart("file") MultipartFile file,@RequestPart("data") TaskSubmitInputDto taskSubmitInputDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("submit task details{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.submitEmployeeCourseTask(file,taskSubmitInputDto), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while no details found ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            logger.error("error occurred while put task file ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateTask(@RequestPart("file") MultipartFile multipartFile, @RequestPart("data") CourseTasksDetailsOutputDto courseTasksDetailsOutputDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("update task details {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseTasksDetailsService.updateTask(multipartFile, courseTasksDetailsOutputDto), HttpStatus.OK);
        }  catch (CustomValidationsException e) {
            logger.error("error occurred while task details are wrong ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            logger.error("error occurred while upload task file ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
