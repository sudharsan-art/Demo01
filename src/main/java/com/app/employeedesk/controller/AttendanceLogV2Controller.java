package com.app.employeedesk.controller;

import com.app.employeedesk.dto.AttendancePunchDTO;
import com.app.employeedesk.service.AttendanceLogV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-log/v2")
public class AttendanceLogV2Controller {
    @Autowired
    private AttendanceLogV2Service service;

    @PostMapping("/punch")
    public String punch(@RequestBody AttendancePunchDTO dto) {
        return service.punch(dto);
    }
}
