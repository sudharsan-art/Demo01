package com.app.employeedesk.validation;

import com.app.employeedesk.dto.AttendanceFilterSearchDTO;
import com.app.employeedesk.dto.AttendanceUpdateDto;
import com.app.employeedesk.dto.EmployeeAttendanceUpdateDto;
import com.app.employeedesk.dto.HolidayPresentComboDto;
import com.app.employeedesk.entity.AttendanceUpdate;
import com.app.employeedesk.entity.ComboOffLeaveRequest;
import com.app.employeedesk.enumeration.UpdateStatus;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.service.EmployeePersonalDetailsService;
import com.app.employeedesk.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class
AttendanceReportSearchValidations {
    private final MessageService messageService;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    public void attendanceFilterValidate(AttendanceFilterSearchDTO attendanceFilterSearchDTO, List<String> errorMessage){
        try{
            attendanceFilterSearchDTO.setMonth(attendanceFilterSearchDTO.getMonth().toUpperCase());
           attendanceFilterSearchDTO.setMonthA(Month.valueOf(attendanceFilterSearchDTO.getMonth().toUpperCase()).getValue());
        }catch (Exception e){
            errorMessage.add(messageService.messageResponse("month.invalid"));
        }
        try{
            Integer year= Integer.parseInt(attendanceFilterSearchDTO.getYear());
            attendanceFilterSearchDTO.setYearA(year);
            if(year<=0 && year> LocalDate.now().getYear() || year<1900){
                errorMessage.add(messageService.messageResponse("year.invalid"));
            }
        }catch (Exception e){
            errorMessage.add(messageService.messageResponse("year.invalid"));
        }
        if(attendanceFilterSearchDTO.getYearA() ==null){
            errorMessage.add(messageService.messageResponse("year.not.null"));
        }
    }

    public void attendanceUpdateValidation(AttendanceUpdate attendanceUpdate, EmployeeAttendanceUpdateDto attendanceUpdateDto, List<String> errorMessage) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm a");
        if(attendanceUpdateDto.getDate()!=null){
            attendanceUpdate.setAttendanceDate(LocalDate.parse(attendanceUpdateDto.getDate(),formatter));
        }else {
            errorMessage.add(messageService.messageResponse("date.not.null"));
        }
        if(!attendanceUpdateDto.getInTime().isBlank() && attendanceUpdateDto.getInTime().length() < 10 && (attendanceUpdateDto.getUpdateField().equals("InTime") || attendanceUpdateDto.getUpdateField().equals("both"))){
            try {
                attendanceUpdate.setInTime( LocalTime.parse(attendanceUpdateDto.getInTime(),format));
            }catch (Exception e){
                errorMessage.add(messageService.messageResponse("check.in.invalid"));
            }
        }
        if (!attendanceUpdateDto.getOutTime().isBlank() && attendanceUpdateDto.getOutTime().length()<10 && (attendanceUpdateDto.getUpdateField().equals("OutTime") || attendanceUpdateDto.getUpdateField().equals("both"))){
            try{
                attendanceUpdate.setOutTime(LocalTime.parse(attendanceUpdateDto.getOutTime(),format));
            }catch (Exception e){
                errorMessage.add(messageService.messageResponse("check.out.invalid"));
            }
        }
        if(attendanceUpdateDto.getReason() != null && attendanceUpdateDto.getReason().length()<256){
            attendanceUpdate.setReason(attendanceUpdate.getReason());
        }

        if(attendanceUpdateDto.getUpdateField()== null || !(attendanceUpdateDto.getUpdateField().equals("InTime") || attendanceUpdateDto.getUpdateField().equals("OutTime")
                || attendanceUpdateDto.getUpdateField().equals("both"))){
            errorMessage.add(messageService.messageResponse("attendance.update.field.not.null"));

        }else {
            attendanceUpdate.setUpdateField(attendanceUpdateDto.getUpdateField());
        }
        if(attendanceUpdateDto.getEmployeeBasicDetails() != null){
            attendanceUpdate.setEmployeeBasicDetails(employeePersonalDetailsService.findEmployeeById(attendanceUpdateDto.getEmployeeBasicDetails()));
        }
        if(attendanceUpdateDto.getUpdateField().equals("both") && attendanceUpdate.getOutTime().isBefore(attendanceUpdate.getInTime())){
            errorMessage.add(messageService.messageResponse("intime.outtime.invalid"));// equal

            if (attendanceUpdateDto.getUpdateStatus() != null) {
                switch (attendanceUpdateDto.getUpdateStatus().toLowerCase()) {
                    case "accepted":
                        attendanceUpdate.setUpdateStatus(UpdateStatus.ACCEPTED);
                        break;
                    case "rejected":
                        attendanceUpdate.setUpdateStatus(UpdateStatus.REJECTED);
                        break;
                    case "pending":
                        attendanceUpdate.setUpdateStatus(UpdateStatus.PENDING);
                        break;
                    case "all":
                        attendanceUpdate.setUpdateStatus(UpdateStatus.ALL);
                        break;
                }
            }
        }
        try{
            LocalTime actualInTime=LocalTime.parse(attendanceUpdateDto.getActualInTime());
            LocalTime actualOutTime=LocalTime.parse(attendanceUpdateDto.getActualOutTime());
            attendanceUpdate.setActualInTime(actualInTime);
            attendanceUpdate.setActualOutTime(actualOutTime);

        }catch (Exception e){
            errorMessage.add(messageService.messageResponse("actual.in.out.time.incorrect"));
        }



    }
    public void comboOffEntryValidations(HolidayPresentComboDto dto, List<String> errorMessage, ComboOffLeaveRequest  comboOff){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy/MM/dd");

        try {
            comboOff.setDate(LocalDate.parse(dto.getHolidayDate(), formatter));

        }catch (Exception e){
            errorMessage.add(messageService.messageResponse("invalid time"));
        }
       if(dto.getHolidayType()==null){
            errorMessage.add(messageService.messageResponse("holiday type cannot be null"));
        }
        if(dto.getWorkSummary()==null){
            errorMessage.add(messageService.messageResponse("work summary cannot be null"));
        }
        if(dto.getTotalHoursWorked()==null){
            errorMessage.add(messageService.messageResponse("worked hours cannot be null"));
        }


    }

}
