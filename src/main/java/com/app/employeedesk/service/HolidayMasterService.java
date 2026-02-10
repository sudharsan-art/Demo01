package com.app.employeedesk.service;

import com.app.employeedesk.dto.FindSiftId;
import com.app.employeedesk.dto.HolidayDto;
import com.app.employeedesk.entity.Holiday;
import com.app.employeedesk.exception.CustomValidationsException;

import com.app.employeedesk.repo.HolidayRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.validation.HolidayValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolidayMasterService {
    private final HolidayRepository holidayRepository;

    private final MessageService messageService;

    private final HolidayValidation holidayValidation;

    private final EmployeeShiftWeekOffService employeeShiftWeekOffService;

    public String createNewHoliday(HolidayDto dto) {
         holidayValidation.saveValidate(dto);
         Holiday holidays=holidayRepository.findByDate(DateUtil.parseLocalDate(dto.getDate())).orElse(null);
         if (holidays!=null){
             throw  new CustomValidationsException(messageService.messageResponse("Holiday.date.unique"));
         }
        Holiday holiday = Holiday.builder()
                .date(DateUtil.parseLocalDate(dto.getDate()))
                .reason(dto.getReason())
                .build();
            holidayRepository.save(holiday);
        return messageService.messageResponse("save.success");
    }

    public String deleteHoliday(String holidayId) {
        if (holidayId == null) {
            throw new CustomValidationsException(messageService.messageResponse("Holiday.id.null"));
        }
        holidayRepository.deleteById(UUID.fromString(holidayId));
        return messageService.messageResponse("delete.success");
    }

     public String updateHoliday(HolidayDto dto){
        holidayValidation.saveValidate(dto);
        boolean isHolidayValidation= holidayRepository.isValidHoliday(UUID.fromString(dto.getId()),DateUtil.parseLocalDate(dto.getDate()));
        if (isHolidayValidation){
            throw  new CustomValidationsException(messageService.messageResponse("Holiday.date.unique"));
         }
         Holiday holiday = Holiday.builder()
                 .id(UUID.fromString(dto.getId()))
                 .date(DateUtil.parseLocalDate(dto.getDate()))
                 .reason(dto.getReason())
                 .build();

             holidayRepository.save(holiday);

         return messageService.messageResponse("update.success");
    }
    public List<HolidayDto>getAllHoliday(){
        LocalDate thisYear=LocalDate.now();
        return holidayRepository.findALLThisYearHoliday(thisYear.getYear());
    }
    public List<LocalDate> getAllHolidayDate(){
        LocalDate thisYear=LocalDate.now();
        return holidayRepository.findAllHolidayDateForThisYear(thisYear.getYear());
    }
    public boolean holidayCheck(LocalDate date){
        Holiday holiday=holidayRepository.findByDate(date).orElse(null);
        return holiday != null;
    }

    public boolean checkHolidayOrNot(LocalDate today) {
        Holiday holiday=holidayRepository.findByDate(today).orElse(null);
        boolean checkWeekOf=employeeShiftWeekOffService.checkWeekOff(today);
        return holiday != null || checkWeekOf;
    }
}
