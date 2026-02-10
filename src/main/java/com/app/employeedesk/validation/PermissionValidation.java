package com.app.employeedesk.validation;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.service.HolidayMasterService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.util.IConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionValidation {

    private final LeaveRequestValidation validation;

    private final MessageService messageService;

    private final HolidayMasterService holidayMasterService;


    public void createValidation(PermissionDto dto) {
        if (dto.getDate() == null) {
            throw new CustomValidationsException(messageService.messageResponse("leave.date.null"));
        }
        if (DateUtil.isValidLocalDate(dto.getDate())) {
            throw new CustomValidationsException(messageService.messageResponse("leave.date.not.valid"));
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new CustomValidationsException(messageService.messageResponse("time.is.empty"));
        }
        if (DateUtil.isValidateTime(dto.getStartTime()) && DateUtil.isValidateTime(dto.getEndTime())) {
            throw new CustomValidationsException(messageService.messageResponse("time.format.invalid"));
        }
        if (dto.getReason() == null) {
            throw new CustomValidationsException(messageService.messageResponse("leave.reason.null"));
        }
        if (dto.getReason().length() > 200) {
            throw new CustomValidationsException(messageService.messageResponse("leave.reason.to.much.length"));
        }
        if (holidayMasterService.holidayCheck(DateUtil.parseLocalDate(dto.getDate()))) {
            throw new CustomValidationsException(messageService.messageResponse("permission.can,t.take.in.holidays"));
        }
        findHoursInGivenTime(dto.getStartTime(), dto.getEndTime());

    }

    public void updateValidation(PermissionDto dto) {

        if (dto.getId() == null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.is.null"));
        }
        if (dto.getEmployeeId() == null) {
            throw new CustomValidationsException(messageService.messageResponse("employee.id.null"));
        }
        createValidation(dto);
    }

    public void approvalValidation(LeavePermissionDto dto) {
        if (dto.getLeaveId() == null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.is.null"));
        }
        validation.validateLeaveStatus(dto.getLeaveStatus());
    }

    public void filterValidation(PermissionFilterDto dto) {
        validation.dateValidation(dto.getDate());
        validation.validateLeaveStatus(dto.getStatus());

    }

    public void checkGivenDateIsAvailableInWeekOFDays(String getDate, List<WeekOfDto> week) {

        LocalDate date = DateUtil.parseLocalDate(getDate);
        assert date != null;
        String day = date.getDayOfWeek().name();
        // convert string day to temporal adjuster
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());

        //  find day number in given date
        int weekNumber = getWeekNumber(date, dayOfWeek);

        for (WeekOfDto i : week) {
            if (weekNumber == i.getWeekNo() && i.getDay().equalsIgnoreCase(day)) {
                throw new CustomValidationsException(messageService.messageResponse("weekOf.day.cant.take.permission"));
            }
        }
    }

    public int getWeekNumber(LocalDate date, DayOfWeek dayOfWeek) {
        LocalDate firstMonday = date.with(TemporalAdjusters.firstInMonth(dayOfWeek));
        int weekNumber = 0;
        if (date.equals(firstMonday)) {
            weekNumber = IConstant.ONE;
        } else if (date.equals(firstMonday.plusWeeks(IConstant.ONE))) {
            weekNumber = IConstant.TWO;
        } else if (date.equals(firstMonday.plusWeeks(IConstant.TWO))) {
            weekNumber = IConstant.THREE;
        } else if (date.equals(firstMonday.plusWeeks(IConstant.THREE))) {
            weekNumber = IConstant.FOUR;
        } else if (date.equals(firstMonday.plusWeeks(IConstant.FOUR))) {
            weekNumber = IConstant.FIVE;
        }
        return weekNumber;
    }

    public void startAndEndTimeValidation(String endTime, String startTime, LocalTime siftStartTime, LocalTime siftEndTime) {
        LocalTime start = DateUtil.parseLocalTime(startTime);
        LocalTime end = DateUtil.parseLocalTime(endTime);
        assert end != null;
        assert start != null;
        if (end.isBefore(start)) {
            throw new CustomValidationsException(messageService.messageResponse("permission.start.time.is.before.end.time"));
        }
        if (!(start.isAfter(siftStartTime)&&end.isBefore(siftEndTime))) {
            throw new CustomValidationsException(messageService.messageResponse("permission.start.end.time.give.properly"));
        }
    }

    public void findHoursInGivenTime(String time1, String time2) {
        LocalTime startTime = DateUtil.parseLocalTime(time1);
        LocalTime endTime = DateUtil.parseLocalTime(time2);
        assert startTime != null;
        long startTimeInMinutes = (long) startTime.getHour() * 60 + startTime.getMinute();
        assert endTime != null;
        long endTimeInMinutes = (long) endTime.getHour() * 60 + endTime.getMinute();

        long totalMinutes = endTimeInMinutes - startTimeInMinutes;
        long totalHours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;


        if (totalHours >= 3  && remainingMinutes >= 0) {
            throw new CustomValidationsException(messageService.messageResponse("permission.time.allowed.3.hr"));

        }
    }
}