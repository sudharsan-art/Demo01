package com.app.employeedesk.controller;

import com.app.employeedesk.dto.LateTrackerDTO;
import com.app.employeedesk.repo.LateTrackerV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/late-tracker")
public class LateTrackerController {
    @Autowired
    private LateTrackerV2Repository lateTrackerRepository;

    @GetMapping("/all")
    public List<LateTrackerDTO> getAllLateTracker() {

        return lateTrackerRepository.findAll()
                .stream()
                .map(t -> LateTrackerDTO.builder()
                        .employeeId(t.getEmployee().getId().toString())
                        .employeeName(t.getEmployee().getName())
                        .month(t.getMonth())
                        .year(t.getYear())
                        .totalLateCount(t.getTotalLateCount())
                        .deductedLeaveCount(t.getDeductedLeaveCount())
                        .build())
                .toList();
    }
}
