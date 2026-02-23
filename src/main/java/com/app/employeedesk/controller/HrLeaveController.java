package com.app.employeedesk.controller;

import com.app.employeedesk.dto.LeaveApprovalV2Dto;
import com.app.employeedesk.service.LeaveServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hr/leaves")
@RequiredArgsConstructor
public class HrLeaveController {

    private final LeaveServiceV2 leaveServiceV2;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable UUID id, @RequestBody LeaveApprovalV2Dto leaveApprovalV2Dto) {
        leaveServiceV2.approveLeave(id, leaveApprovalV2Dto.getStatus());
        return ResponseEntity.ok("Leave status updated successfully");
    }
}
