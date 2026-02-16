package com.app.employeedesk.controller;


import com.app.employeedesk.dto.EmployeeLeaveConfigViewDto;
import com.app.employeedesk.dto.LeavePolicyDto;
import com.app.employeedesk.response.Response;
import com.app.employeedesk.response.ResponseGenerator;
import com.app.employeedesk.response.TransactionContext;
import com.app.employeedesk.service.LeavePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leave/policy")
public class LevePolicyController {

    private final LeavePolicyService leavePolicyService;
    private final ResponseGenerator responseGenerator;

    @GetMapping("/my-config")
    public ResponseEntity<Response>myConfig(@RequestHeader HttpHeaders headers, Principal principal){
        TransactionContext ctx= responseGenerator.generateTransationContext(headers);
        EmployeeLeaveConfigViewDto data=leavePolicyService.getEmployeeLEaveConfig(principal);
        return responseGenerator.successResponse(ctx,data, HttpStatus.OK);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Response> byRole(@PathVariable String role, @RequestHeader HttpHeaders headers) {
        TransactionContext ctx = responseGenerator.generateTransationContext(headers);
        List<LeavePolicyDto> data = leavePolicyService.getPoliciesByRole(role);
        return responseGenerator.successResponse(ctx, data, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody LeavePolicyDto dto, @RequestHeader HttpHeaders headers) {
        TransactionContext ctx = responseGenerator.generateTransationContext(headers);
        LeavePolicyDto data = leavePolicyService.createOrUpdate(dto);
        return responseGenerator.successResponse(ctx, data, HttpStatus.OK);
    }
}
