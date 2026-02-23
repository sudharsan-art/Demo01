package com.app.employeedesk.controller;

import com.app.employeedesk.dto.LeaveRequestV2Dto;
import com.app.employeedesk.security.JwtService;
import com.app.employeedesk.service.LeaveServiceV2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor

public class LeaveControllerV2 {
    private final LeaveServiceV2 leaveServiceV2;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> applyLeave(@RequestBody LeaveRequestV2Dto leaveRequestV2Dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtService.extractUserName(token);

        leaveServiceV2.applyLeave(leaveRequestV2Dto, email);

        return ResponseEntity.ok("Leave applied successfully");
    }
}
