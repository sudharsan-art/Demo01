package com.app.employeedesk.controller;

import com.app.employeedesk.dto.OdRequestDto;
import com.app.employeedesk.entity.OdRequest;
import com.app.employeedesk.service.OdRequestService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/od" )
@RequiredArgsConstructor

public class OdRequestController {
    private final OdRequestService odRequestService;
    @PostMapping("/apply")
    public ResponseEntity<String> applyOdRequest(@RequestBody OdRequestDto request) {

        String response = odRequestService.applyOdRequest(
                request.getEmployeeId(),
                LocalDate.parse(request.getOdDate()),
                request.getReason(),
                request.getLocation()
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping ("/employee/{employeeId}")
    public ResponseEntity<List<OdRequest>> getEmployeeOdRequests(
            @PathVariable String employeeId) {

        List<OdRequest> list = odRequestService.getEmployeeOdRequests(employeeId);

        return ResponseEntity.ok(list);
    }
}

