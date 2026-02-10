package com.app.employeedesk.service;

import com.app.employeedesk.bean.ComboOffIntimeOutTime;
import com.app.employeedesk.dto.ComboOffResponseDto;
import com.app.employeedesk.dto.HolidayPresentComboDto;
import com.app.employeedesk.entity.ComboOffLeaveRequest;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.Holiday;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.TimeIncorrectException;
import com.app.employeedesk.repo.AttendanceRepository;
import com.app.employeedesk.repo.ComboOffRepository;
import com.app.employeedesk.repo.HolidayRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.AttendanceReportSearchValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboOffService {
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final AttendanceRepository attendanceRepository;
    private final HolidayRepository holidayRepository;
    private final AttendanceReportSearchValidations attendanceReportSearchValidations;
    private final ComboOffRepository comboOffRepository;
    private final MessageService messageService;


    public ComboOffResponseDto getComboInfoBasedDate(Principal principal,String date){
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate comboDate;
        try {
            comboDate =LocalDate.parse(date,format);
        } catch (Exception e) {
            throw new TimeIncorrectException(e.getMessage());
        }
        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);

        ComboOffIntimeOutTime localTimes = attendanceRepository.getTimeForComboOff(employeeBasicDetails.getId(),comboDate);
        int workedTime=(int)localTimes.getInTime().until(localTimes.getOutTime(), ChronoUnit.HOURS);
        Holiday holiday=holidayRepository.checkHolidayOrNot(comboDate);
        if(workedTime<3 && holiday==null){
            throw new TimeIncorrectException("Noo Combo Off eligible");
        }
        return ComboOffResponseDto.builder()
                .totalHoursWorked(workedTime)
                .holidayType(holiday.getReason())
                .date(comboDate).build();
    }

    public Object saveEmployeeRequest(HolidayPresentComboDto dto,Principal principal){
        List<String> errorMessage=new LinkedList<>();
        ComboOffLeaveRequest comboOff = new ComboOffLeaveRequest();

        attendanceReportSearchValidations.comboOffEntryValidations(dto,errorMessage,comboOff);
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        EmployeeBasicDetails details=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        comboOff.setName(dto.getName());
        comboOff.setDepartment(String.valueOf(details.getDepartment()));
        comboOff.setEmployeeBasicDetails(details);
        comboOff.setWorkedHours(dto.getTotalHoursWorked());
        comboOff.setWorkSummary(dto.getWorkSummary());
        comboOffRepository.save(comboOff);
        return messageService.messageResponse("updated successfully");














    }

}
