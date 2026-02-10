package com.app.employeedesk.controller;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/EmployeeDetails")
@RequiredArgsConstructor
public class EmployeePersonelDetailsController {
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final CountryService countryService;
    private final StateService stateService;
    private final EmployeeExperienceService employeeExperienceService;
    private final EducationalDetailsService educationalDetailsService;
    private final EmployeeSkillsService employeeSkillsService;
    public final EmployeeDocumentsService employeeDocumentsService;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ResponseGenerator responseGenerator;




    @GetMapping("/GetBasicDetailsTab")
    public ResponseEntity<Response> getBasicDetailsFromTable(Principal principal, @RequestHeader HttpHeaders httpHeaders) {
        logger.info("get basic details of employee {}", LocalDateTime.now());
        TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
        try {
            String username=principal.getName();

            return responseGenerator.successResponse(context,employeePersonalDetailsService.personalDetailsGet(username) , HttpStatus.OK);
        } catch (CustomValidationsException e) {
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetCountry")
    public ResponseEntity<Response> getCountry(@RequestHeader HttpHeaders httpHeaders){
        logger.info("get Country {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,countryService.viewAll(),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);

        }
    }
    @GetMapping("/GetState")
    public ResponseEntity<Response> getState(@RequestBody CountryDto countryDto, @RequestHeader HttpHeaders httpHeaders){
        logger.info("get state {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,countryService.findStateByCountry(countryDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetCity")
    public ResponseEntity<Response> getCity(@RequestBody StateDto stateDto,@RequestHeader HttpHeaders httpHeaders){
        logger.info("get city {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,stateService.findCityByState(stateDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/EmpPerDetUpt")
    public ResponseEntity<Response> updateEmployeePersonalDetailsTab(@RequestBody EmployeePersonalDetailsTab employeePersonalDetailsTab,@RequestHeader HttpHeaders httpHeaders){
        logger.info("update the employee Personal Detail {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeePersonalDetailsService.employeePersonalDetailsUpdate(employeePersonalDetailsTab ),HttpStatus.OK);

        } catch (ListOfValidationException e) {
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
        catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetExperience")
    public ResponseEntity<Response> getExperience(@RequestBody UUID employeeId,@RequestHeader HttpHeaders httpHeaders){
        logger.info("update the employee Personal Details {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeExperienceService.getEmployeeWorkExperience(employeeId),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/AddExperience")
    public ResponseEntity<Response> addExperience(@RequestBody EmployeeWorkExperienceDto employeeWorkExperienceDto ,@RequestHeader HttpHeaders httpHeaders){
        logger.info("Adding the experience {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeExperienceService.addexperience(employeeWorkExperienceDto),HttpStatus.BAD_REQUEST);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/deleteExperience")
    public ResponseEntity<Response> deleteExperience(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeaders){
        logger.info("Delete the experience {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeExperienceService.deleteExperience(id),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetExperienceByIndividual")
    public ResponseEntity<Response> getExperienceByIndividual(@RequestBody UUID experienceId,@RequestHeader HttpHeaders httpHeaders){
        logger.info("update the employee Personal Details {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeExperienceService.updateEmployeeExperience(experienceId),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetEducationDetailByUser")
    public ResponseEntity<Response> getEducationDetailByUser(@RequestBody UUID userId,@RequestHeader HttpHeaders httpHeaders){
        logger.info("get the educational details of the user {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,educationalDetailsService.getEducationalDetails(userId),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetUserEducationalDetails")
    public ResponseEntity<Response> getUserEducationalDetails(@RequestBody EducationalDetailsDto educationalDetailsDto,@RequestHeader HttpHeaders httpHeaders){
        logger.info("get the educational details by id  {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,educationalDetailsService.getUserEducationDetails(educationalDetailsDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/PostEducationDetails")
    public ResponseEntity<Response> postEducationalDetails(@RequestBody EducationalDetailsDto educationalDetailsDto, @RequestHeader HttpHeaders httpHeaders){
        logger.info("get the educational details of the user {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,educationalDetailsService.postEducationalDetails(educationalDetailsDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/DeteleteQualification")
    public ResponseEntity<Response> deleteEmployeeQualification(@RequestBody EducationalDetailsDto educationalDetailsDto,@RequestHeader HttpHeaders httpHeaders){
        logger.info("Deleteing the employee experience based on the id  {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,educationalDetailsService.deleteEducationDetails(educationalDetailsDto),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/GetEducationDropdown")
    public ResponseEntity<Response> educationDropdown(@RequestHeader HttpHeaders httpHeaders){
        logger.info("Deleteing the employee experience based on the id  {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,educationalDetailsService.getQualificationDropdown(),HttpStatus.OK);

        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping ("/getAllEmployeeDetails")
    public ResponseEntity<Response> getAllEmployeeDetails(@RequestHeader HttpHeaders httpHeaders){
        logger.info("get all employee details  {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeePersonalDetailsService.getAllEmployeeDetails(),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.info("error occur while employee details are wrong  {}",LocalDateTime.now());
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getSkillDropdown")
    public ResponseEntity<Response> getSkillsDropdown(@RequestHeader HttpHeaders httpHeaders){
        logger.info("get all the skill dropdown  {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeSkillsService.getSkillsForDropdown(),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.info("error occur while details get{}",LocalDateTime.now());
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getSkillsForEmployee")
    public ResponseEntity<Response> getSkillsForEmployees(Principal principal, @RequestHeader HttpHeaders httpHeaders){
        logger.info("get skills based on the employee login {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeSkillsService.getSkillsForEmployee(principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.info("error occur while employee skills get  {}",LocalDateTime.now());
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    @PostMapping("/addSkillForEmployee")
    public ResponseEntity<Response> addSkillForEmployee(@RequestBody EmployeeSkillsDto employeeSkillsDto,Principal principal,@RequestHeader HttpHeaders httpHeaders){
        logger.info("add skill for the employee {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeSkillsService.addSkills(employeeSkillsDto,principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.info("error occur while employee skill add  {}",LocalDateTime.now());
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/deleteSkillForEmployee")
    public ResponseEntity<Response> deleteSkillForEmployee(@RequestBody EmployeeSkillsDto employeeSkillsDto,Principal principal,@RequestHeader HttpHeaders httpHeaders){
        logger.info("delete skill for the employee {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeSkillsService.deleteSkills(employeeSkillsDto,principal),HttpStatus.OK);
        }catch (CustomValidationsException e){
            logger.info("error occur while employee skill delete  {}",LocalDateTime.now());
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getDocumentsName")
    public ResponseEntity<Response> getDocumentsNeeds(@RequestHeader HttpHeaders httpHeaders){
        logger.info("get the required documents name for the employee {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeeDocumentsService.getdocumentsName(),HttpStatus.OK);
        } catch (CustomValidationsException e) {
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/saveDocuments")
    public ResponseEntity<Response> saveDocuments(@RequestPart("files") List<MultipartFile> documents,Principal principal,@RequestPart("json") EmployeeDocumentsDtoList employeeDocumentsDtoList, @RequestHeader HttpHeaders httpHeaders){

        logger.info("get the employee documents from the front end and save to the database {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {
            return responseGenerator.successResponse(context,employeeDocumentsService.saveEmployeeDocuments(documents,employeeDocumentsDtoList,principal),HttpStatus.OK);
        } catch (CustomValidationsException | IOException e) {
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (ListOfValidationException e){
            return responseGenerator.errorResponses(context,e.getErrorMessages(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value="/getSavedDocuments" , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Response> getSavesDocuments(Principal principal, @RequestHeader HttpHeaders httpHeaders){
        logger.info("get the documents saved for the employee {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try {

            return responseGenerator.successResponse(context,employeeDocumentsService.documentsGet(principal),HttpStatus.OK);

        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/deleteDocuments")
    public ResponseEntity<Response> deleteDocuments(@RequestBody UUID id,@RequestHeader HttpHeaders httpHeaders){
        logger.info("To delete the employee documents {}",LocalDateTime.now());
        TransactionContext context=responseGenerator.generateTransationContext(httpHeaders);
        try{
            return responseGenerator.successResponse(context,employeeDocumentsService.deleteDocuments(id),HttpStatus.OK);
        }catch (CustomValidationsException e){
            return responseGenerator.errorResponse(context,e.getMessage(),HttpStatus.BAD_REQUEST);

        }

    }
}
