package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeeSkillsDto;
import com.app.employeedesk.dto.EmployeeSkillsForEmployeeDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeSkillsForEmployee;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeeSkills;
import com.app.employeedesk.repo.EmployeeSkillsForEmployeeRepository;
import com.app.employeedesk.response.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeSkillsService {
    private final EmployeeSkills employeeSkillsRepository;
    private final EmployeeSkillsForEmployeeRepository employeeSkillsForEmployeeRepository;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final MessageService messageService;

    public List<EmployeeSkillsDto> getSkillsForDropdown(){
        List<com.app.employeedesk.entity.EmployeeSkills> skillsList=employeeSkillsRepository.findAll();
        return skillsList.stream().map(o-> EmployeeSkillsDto.builder()
                .id(o.getId())
                .skillName(o.getSkillName()).build()).toList();
    }



    public List<EmployeeSkillsDto> getSkillsForEmployee(Principal principal) throws JsonProcessingException {
        EmployeeBasicDetails employeeBasicDetails = employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        ObjectMapper mapper=new ObjectMapper();
        EmployeeSkillsForEmployee employeeSkillsForEmployee=employeeSkillsForEmployeeRepository.findByEmployeeBasicDetails(employeeBasicDetails);
        try {
            TypeReference<List<EmployeeSkillsDto>> typeReference = new TypeReference<List<EmployeeSkillsDto>>() {};
            return mapper.readValue(employeeSkillsForEmployee.getEmployeeSkillsJson(), typeReference);
        } catch (Exception e) {
           throw new  CustomValidationsException(messageService.messageResponse(e.getMessage()));
        }


    }
    public String addSkills(EmployeeSkillsDto skills, Principal principal ) throws JsonProcessingException {
        if(skills !=null){
            ObjectMapper obj=new ObjectMapper();
            EmployeeBasicDetails basicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
            EmployeeSkillsForEmployee employeeSkillsForEmployee=employeeSkillsForEmployeeRepository.findByEmployeeBasicDetails(basicDetails);
            TypeReference<List<EmployeeSkillsDto>> jsonList=new TypeReference<>() {};
            List<EmployeeSkillsDto> employeeSkillsDtoList=new ArrayList<>();
            boolean flag=false;
            UUID id=null;

            if(employeeSkillsForEmployee != null) {
                 id=employeeSkillsForEmployee.getId();
                flag=true;

                employeeSkillsDtoList=obj.readValue(employeeSkillsForEmployee.getEmployeeSkillsJson(),jsonList);
                for (EmployeeSkillsDto i : employeeSkillsDtoList) {
                    if (skills.getSkillName().equals(i.getSkillName())) {
                        throw new CustomValidationsException(messageService.messageResponse("skill.already.present"));
                    }
                }
                employeeSkillsDtoList.add(skills);
//
            }else{
                employeeSkillsDtoList.add(skills);
//

            }



            String storeJson= obj.writeValueAsString(employeeSkillsDtoList);
            EmployeeSkillsForEmployee employeeSkillsForEmployee1=new EmployeeSkillsForEmployee();
            if(flag){
                employeeSkillsForEmployee1.setId(id);
            }

            employeeSkillsForEmployee1.setEmployeeBasicDetails(basicDetails);
            employeeSkillsForEmployee1.setEmployeeSkillsJson(storeJson);
            employeeSkillsForEmployee1.setSkillId(skills.getId());
            employeeSkillsForEmployeeRepository.save(employeeSkillsForEmployee1);
        }
        return messageService.messageResponse("skill.saved.successfully");

    }
    public String deleteSkills(EmployeeSkillsDto skills, Principal principal) throws JsonProcessingException {
        if(skills !=null){
            ObjectMapper obj=new ObjectMapper();
            EmployeeBasicDetails basicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
            EmployeeSkillsForEmployee employeeSkillsForEmployee=employeeSkillsForEmployeeRepository.findByEmployeeBasicDetails(basicDetails);
            TypeReference<List<EmployeeSkillsDto>> jsonList=new TypeReference<List<EmployeeSkillsDto>>() {};
            List<EmployeeSkillsDto> employeeSkillsDtos;
            employeeSkillsDtos=obj.readValue(employeeSkillsForEmployee.getEmployeeSkillsJson(),jsonList);
            employeeSkillsDtos.removeIf(i->i.getSkillName().equals(skills.getSkillName()));
            String storeJson= obj.writeValueAsString(employeeSkillsDtos);
            EmployeeSkillsForEmployee employeeSkillsForEmployee1=new EmployeeSkillsForEmployee();
            employeeSkillsForEmployee1.setId(employeeSkillsForEmployee.getId());
            employeeSkillsForEmployee1.setEmployeeBasicDetails(basicDetails);
            employeeSkillsForEmployee1.setEmployeeSkillsJson(storeJson);
            employeeSkillsForEmployee1.setSkillId(employeeSkillsForEmployee.getSkillId());
            employeeSkillsForEmployeeRepository.save(employeeSkillsForEmployee1);
        }
        return messageService.messageResponse("skill.deleted.success");

        
    }



}
