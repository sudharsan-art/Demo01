package com.app.employeedesk.controller;
import com.app.employeedesk.dto.LeaveMasterDto;
import com.app.employeedesk.service.LeaveMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-master")
@RequiredArgsConstructor
public class LeaveMasterController {

    private final LeaveMasterService leaveMasterService;

    @GetMapping
    public List<LeaveMasterDto> getLeaveTypes() {
        return leaveMasterService.getAllLeaves();
    }
}