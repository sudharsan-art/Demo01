package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AttendancePolicyDTO;
import com.app.employeedesk.entity.AttendancePolicyV2;
import com.app.employeedesk.repo.AttendancePolicyV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-policy")
public class AttendancePolicyController {
    @Autowired
    private AttendancePolicyV2Repository policyRepository;

    @GetMapping
    public AttendancePolicyDTO getPolicy() {

        AttendancePolicyV2 policy = policyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        return AttendancePolicyDTO.builder()
                .id(policy.getId())
                .officeStartTime(policy.getOfficeStartTime())
                .officeEndTime(policy.getOfficeEndTime())
                .lateGraceMinutes(policy.getLateGraceMinutes())
                .maxLateAllowed(policy.getMaxLateAllowed())
                .build();
    }
}
