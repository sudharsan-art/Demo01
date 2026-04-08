package com.app.employeedesk.controller;

import com.app.employeedesk.dto.YearMasterDTO;
import com.app.employeedesk.repo.YearMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/year-master")
public class YearMasterController {
    @Autowired
    private YearMasterRepository yearMasterRepository;

    @GetMapping
    public List<YearMasterDTO> getAllYears() {

        return yearMasterRepository.findAll()
                .stream()
                .map(y -> YearMasterDTO.builder()
                        .id(y.getId().toString())
                        .yearName(y.getYearName())
                        .startDate(y.getStartDate())
                        .endDate(y.getEndDate())
                        .active(y.getActive())
                        .build())
                .toList();
    }
}
