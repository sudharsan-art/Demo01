package com.app.employeedesk.service;

import com.app.employeedesk.bean.WeekOfBean;
import com.app.employeedesk.dto.EmployeeShiftDto;
import com.app.employeedesk.dto.EmployeeWeekOffDto;
import com.app.employeedesk.dto.FindSiftId;
import com.app.employeedesk.dto.WeekOfDto;
import com.app.employeedesk.entity.EmployeeShiftEntity;
import com.app.employeedesk.entity.EmployeeWeekOff;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.EmployeeShitRepository;
import com.app.employeedesk.repo.PermissionRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.util.IConstant;
import com.app.employeedesk.validation.EmployeeShiftMasterValidation;
import com.app.employeedesk.validation.PermissionValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmployeeShiftWeekOffService {

    private final EmployeeShiftMasterValidation employeeShiftMasterValidation;

    private final EmployeeShitRepository employeeShitRepository;

    private final MessageService messageService;


    private final PermissionRepository permissionRepository;


    public String shiftCreate(EmployeeShiftDto employeeShiftDto) {
        List<LocalTime> dates = new ArrayList<>();
        List<String> errorMessage = new ArrayList<>();
        employeeShiftMasterValidation.shiftValidation(employeeShiftDto, errorMessage, dates);
        if (!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }

        EmployeeShiftEntity employeeShift = EmployeeShiftEntity.builder()
                .id(employeeShiftDto.getId())
                .shiftName(employeeShiftDto.getShiftName())
                .shiftStartTime(dates.get(0))
                .shiftEndTime(dates.get(1))
                .firstHalfStartTime(dates.get(2))
                .firstHalfEndTime(dates.get(3))
                .secoundHalfStartTime(dates.get(4))
                .secoundHalfEndTime(dates.get(5))
                .remarks(employeeShiftDto.getRemarks())
                .build();
        if (employeeShiftDto.getEmployeeWeekOff() != null) {
            List<EmployeeWeekOff> employeeWeekOff = employeeShiftDto.getEmployeeWeekOff().stream()
                    .map(o -> weekOfDtoToEntity(o, employeeShift)).toList();
            employeeShift.setEmployeeWeekOff(employeeWeekOff);
        }
        employeeShitRepository.save(employeeShift);
        return messageService.messageResponse("saved.successfuly");
    }

    public EmployeeWeekOff weekOfDtoToEntity(EmployeeWeekOffDto employeeWeekOffDto, EmployeeShiftEntity employeeShift) {
        return EmployeeWeekOff.builder()
                .shiftid(employeeShift)
                .weekNo(employeeWeekOffDto.getWeekNo())
                .Day(employeeWeekOffDto.getDay())
                .id(employeeWeekOffDto.getId())
                .dayStatus(employeeWeekOffDto.getDayStatus()).build();
    }

    public EmployeeShiftDto getEmployeeShiftById(UUID id) {
        Optional<EmployeeShiftEntity> employeeShiftEntity = employeeShitRepository.findById(id);
        if (employeeShiftEntity.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse("shift.not.present"));
        }
        EmployeeShiftDto employeeShiftDto = EmployeeShiftDto.builder()
                .id(employeeShiftEntity.get().getId())
                .shiftName(employeeShiftEntity.get().getShiftName())
                .shiftStartTime(DateUtil.parseStringTme(employeeShiftEntity.get().getShiftStartTime()))
                .shiftEndTime(DateUtil.parseStringTme(employeeShiftEntity.get().getShiftEndTime()))
                .firstHalfStartTime(DateUtil.parseStringTme(employeeShiftEntity.get().getFirstHalfStartTime()))
                .firstHalfEndTime(DateUtil.parseStringTme(employeeShiftEntity.get().getFirstHalfEndTime()))
                .secoundHalfStartTime(DateUtil.parseStringTme(employeeShiftEntity.get().getSecoundHalfStartTime()))
                .secoundHalfEndTime(DateUtil.parseStringTme(employeeShiftEntity.get().getSecoundHalfEndTime()))
                .remarks(employeeShiftEntity.get().getRemarks())
                .build();
        if (!employeeShiftEntity.get().getEmployeeWeekOff().isEmpty()) {
            List<EmployeeWeekOffDto> employeeWeekOff = employeeShiftEntity.get().getEmployeeWeekOff().stream().map(this::weekOffEntityToDto).toList();
            employeeShiftDto.setEmployeeWeekOff(employeeWeekOff);
        }
        return employeeShiftDto;

    }

    public EmployeeWeekOffDto weekOffEntityToDto(EmployeeWeekOff employeeWeekOff) {
        return EmployeeWeekOffDto.builder()
                .id(employeeWeekOff.getId())
                .day(employeeWeekOff.getDay())
                .weekNo(employeeWeekOff.getWeekNo())
                .dayStatus(employeeWeekOff.getDayStatus())
                .build();
    }

    public String deleteShift(UUID id) {
        employeeShitRepository.findById(id).ifPresent(o -> {
            if (o.getEmployeeBasicDetails().isEmpty()) {
                employeeShitRepository.deleteById(id);
            } else {
                throw new CustomValidationsException(messageService.messageResponse("shift.associated.with.child"));
            }
        });
        return messageService.messageResponse("deleted.success");

    }
//    public boolean checkPermissionTime(LocalTime fromTime ,LocalTime endTime ,UUID empId){
//     boolean isValidPermissionTime=employeeShitRepository.checkSiftTiming(fromTime,endTime,empId);
//        return false;
//    }


    public List<EmployeeWeekOff> findEmployeeSiftWeekOf(UUID id) {
        return employeeShitRepository.findByEmployeeWeekOff(id);
    }

    public boolean checkWeekOff(LocalDate today) {
        DayOfWeek day = today.getDayOfWeek();
        FindSiftId sift = new FindSiftId();
        List<WeekOfBean> weekOfBean = permissionRepository.findId(sift.getId());
        List<WeekOfDto> weeks = beanToDto(weekOfBean);
        int weekNumber = getWeekNumber(today, day);
        for (WeekOfDto week : weeks) {
            if (weekNumber == week.getWeekNo() && week.getDay().equalsIgnoreCase(String.valueOf(day))) {
                return true;
            }
        }
        return false;
    }

    public List<WeekOfDto> beanToDto(List<WeekOfBean> weekOfBean) {
        List<WeekOfDto> weekOf = new ArrayList<>();
        for (WeekOfBean week : weekOfBean) {
            weekOf.add(WeekOfDto.builder().day(week.getDays()).weekNo(week.getWeekNo()).build());
        }
        return weekOf;
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
}

