package com.app.employeedesk.service;

import com.app.employeedesk.dto.AdminFilterRequestDTO;
import com.app.employeedesk.dto.AdminFilterResponseDTO;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeSkillsForEmployee;
import com.app.employeedesk.entity.EmployeeWorkExperience;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFilterService {
    private final EmployeeBasicDetailsRepository employeeBasicDetailsRepository;

    private Specification<EmployeeBasicDetails> adminFilter(AdminFilterRequestDTO filterDto){
        return (root, query, criteriaBuilder) ->{
            Join<EmployeeBasicDetails, UserDetails> basicDetailsUserJoin=root.join("userDetailsId", JoinType.LEFT);
            Join<EmployeeBasicDetails, EmployeeSkillsForEmployee> basicDetailSkillJoin=root.join("employeeSkillsForEmployees",JoinType.LEFT);
            Join<EmployeeBasicDetails, EmployeeWorkExperience> basicDetailsWorkExperience= root.join("employeeWorkExperienceList",JoinType.LEFT);
            List<Predicate> predicateList=new ArrayList<>();
            if( filterDto.getFirstName().length()<255 && filterDto.getLastName().length()<255){
                if(!filterDto.getFirstName().isEmpty() || !filterDto.getLastName().isEmpty()){
                Predicate firstname=criteriaBuilder.like(root.get("firstName"), filterDto.getFirstName());
                Predicate lastName=criteriaBuilder.like(root.get("lastName"), filterDto.getLastName());
                predicateList.add(criteriaBuilder.or(firstname,lastName));
            }
                }
            if(filterDto.getExperience()!= null){
                Subquery<UUID> subquery= query.subquery(UUID.class);
                Root<EmployeeWorkExperience> employeeWorkExperienceRoot= subquery.from(EmployeeWorkExperience.class);
                subquery.select(employeeWorkExperienceRoot.get("employeeBasicDetails").get("id"))
                        .groupBy(employeeWorkExperienceRoot.get("employeeBasicDetails").get("id"))
                        .having(criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.sum(employeeWorkExperienceRoot.get("experience")), filterDto.getExperience()));

                predicateList.add(criteriaBuilder.in(root.get("id")).value(subquery));
            }
            if(filterDto.getPreviousCompanyName()!= null && !filterDto.getPreviousCompanyName().isEmpty()){
                predicateList.add(criteriaBuilder.like(basicDetailsWorkExperience.get("companyName"),"%"+filterDto.getPreviousCompanyName()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));




        };

    }
    public List<AdminFilterResponseDTO> adminFilterResponse(AdminFilterRequestDTO adminFilterRequestDTO){
      return employeeBasicDetailsRepository.findAll(adminFilter(adminFilterRequestDTO)).stream().map(o->{
          return AdminFilterResponseDTO.builder()
                  .role(String.valueOf(o.getDesigination()))
                  .experience(o.getEmployeeWorkExperienceList().stream().map(EmployeeWorkExperience::getExperience).reduce(0, Integer::sum))
                  .name(o.getFirstName()+" "+o.getLastName())
                  .employeeId(o.getEmployeecode())
                  .contactNo(o.getPhoneNumber())
                  .build();

      }).collect(Collectors.toList());



    }
}
