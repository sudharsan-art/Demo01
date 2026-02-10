package com.app.employeedesk.controller;


import com.app.employeedesk.dto.CourseMaterialsDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.CourseMaterialsService;
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
@RequestMapping("/material")
@RequiredArgsConstructor
public class CourseMaterialsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final @NonNull ResponseGenerator responseGenerator;

    private final CourseMaterialsService courseMaterialsService;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadMaterial(@RequestPart("file") MultipartFile multipartFile, @RequestPart("data") CourseMaterialsDto courseMaterialsDto, @RequestHeader HttpHeaders httpHeader) {
        logger.info("file uploaded {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseMaterialsService.uploadFile(multipartFile, courseMaterialsDto), HttpStatus.OK);
        } catch (ListOfValidationException e) {
            logger.error("error occurred while details are wrong ", e);
            return responseGenerator.errorResponses(context, e.getErrorMessages(), HttpStatus.BAD_REQUEST);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while upload details are wrong ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            logger.error("error occurred while upload file ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadMaterial(@RequestBody UUID materialId) {
        logger.info("file downloaded {}", LocalDateTime.now());
        byte[] excelFile = courseMaterialsService.downloadFile(materialId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment; filename=\"report.xlsx\"", "report.xlsx");
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }

    @GetMapping("/getAllMaterialDetails")
    public ResponseEntity<Response> getAllMaterialDetails(@RequestBody UUID subTopicId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("get all material details{}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseMaterialsService.getAllMaterialDetails(subTopicId), HttpStatus.OK);
        } catch (CustomValidationsException e) {
            logger.error("error occurred while no materials found ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteMaterial(@RequestBody UUID materialId, @RequestHeader HttpHeaders httpHeader) {
        logger.info("file deleted {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, courseMaterialsService.deleteMaterial(materialId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error occurred while delete material  ", e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
