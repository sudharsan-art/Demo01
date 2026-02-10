package com.app.employeedesk.validation;
import com.app.employeedesk.dto.EmployeeShiftDto;
import com.app.employeedesk.dto.EmployeeWeekOffDto;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EmployeeShitRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeShiftMasterValidation {
    private final MessageService messageService;
    private final EmployeeShitRepository employeeShitRepository;

    public void shiftValidation(EmployeeShiftDto employeeShiftDto, List<String> errorMessage, List<LocalTime> dates) {
        if (stringValidation(employeeShiftDto.getShiftName() )) {
            errorMessage.add(messageService.messageResponse("shift.name.invalid"));
        }
        if(stringValidation(employeeShiftDto.getShiftStartTime() ) || DateUtil.isValidateTime(employeeShiftDto.getShiftStartTime())){
            errorMessage.add(messageService.messageResponse("shift.start.time.invalid"));
        }
        if((stringValidation(employeeShiftDto.getShiftEndTime() ) || DateUtil.isValidateTime(employeeShiftDto.getShiftEndTime()))){
            errorMessage.add(messageService.messageResponse("shift.end.time.invalid"));
        }
        if((stringValidation(employeeShiftDto.getFirstHalfStartTime() ) || DateUtil.isValidateTime(employeeShiftDto.getFirstHalfStartTime()))){
            errorMessage.add(messageService.messageResponse("first.half.start.time.invalid"));
        }
        if((stringValidation(employeeShiftDto.getFirstHalfEndTime()) || DateUtil.isValidateTime(employeeShiftDto.getFirstHalfEndTime()))){
            errorMessage.add(messageService.messageResponse("first.half.end.time.invalid"));
        }
        if((stringValidation(employeeShiftDto.getSecoundHalfStartTime()) || DateUtil.isValidateTime(employeeShiftDto.getSecoundHalfStartTime()))){
            errorMessage.add(messageService.messageResponse("secound.half.start.time.invalid"));
        }
        if((stringValidation(employeeShiftDto.getSecoundHalfEndTime() )|| DateUtil.isValidateTime(employeeShiftDto.getSecoundHalfEndTime()))){
            errorMessage.add(messageService.messageResponse("secound.half.end.time.invalid"));
        }
        if(employeeShiftDto.getId()==null&&employeeShitRepository.existsByShiftName(employeeShiftDto.getShiftName())){
            errorMessage.add(messageService.messageResponse("shift.name.duplicate"));
        }
    shiftMaster2(employeeShiftDto,errorMessage,dates);
    }
    public void shiftMaster2(EmployeeShiftDto employeeShiftDto,List<String> errorMessage,List<LocalTime> dates) {
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getShiftStartTime()));
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getShiftEndTime()));
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getFirstHalfEndTime()));
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getFirstHalfStartTime()));
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getSecoundHalfStartTime()));
        dates.add(DateUtil.parseLocalTime(employeeShiftDto.getSecoundHalfEndTime()));

        if (stringValidation(employeeShiftDto.getRemarks())) {
            errorMessage.add(messageService.messageResponse("remarks.incorrect"));
        }


        if (employeeShiftDto.getEmployeeWeekOff() != null) {
            for (EmployeeWeekOffDto i : employeeShiftDto.getEmployeeWeekOff()) {
                if (stringValidation(i.getDay())) {
                    errorMessage.add(messageService.messageResponse("week.off.day.invalid"));
                }
                if (stringValidation(i.getDayStatus())) {
                    errorMessage.add(messageService.messageResponse("week.off.day.status.invalid"));
                }
                if (i.getWeekNo() == null) {
                    errorMessage.add(messageService.messageResponse("week.no.invalid"));
                }
            }
        }
        shiftMaster3(errorMessage,dates);
    }
    public void shiftMaster3(List<String> errorMessage,List<LocalTime> dates){
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }else {
            if (dates.get(0).isAfter(dates.get(1)) || dates.get(2).isAfter(dates.get(3)) || dates.get(4).isAfter(dates.get(5))) {
                errorMessage.add(messageService.messageResponse("start.time.end.time.incorrect"));
            }
        }
    }


    public boolean stringValidation(String validateString){
        return validateString == null || validateString.isBlank() || validateString.length() <= 0 || validateString.length() >= 255;
        }

}




