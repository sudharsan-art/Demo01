package com.app.employeedesk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalTime;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class EmployeeWeekOfMasterController {
    private String shiftName;
    private LocalTime firstHalfStartTime;
    private LocalTime firstHalfEndTime;
    private LocalTime secoundHalfStartTime;
    private LocalTime secoundHalfEndTime;
    private String remarks;

}
