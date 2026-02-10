package com.app.employeedesk.validation;

import com.app.employeedesk.dto.LeavePermissionDto;
import com.app.employeedesk.dto.LeaveRequestDto;
import com.app.employeedesk.entity.EmployeeWeekOff;
import com.app.employeedesk.enumeration.LeaveDayType;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.LeaveType;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.util.IConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Component
@RequiredArgsConstructor
public class LeaveRequestValidation {
    private final MessageService messageService;
    private static final String LEAVE_STATUS_NULL = "leave.status.null";
    private static final String LEAVE_STATUS_NOT_MATCHED = "leave.status.not.match";

    public void updateLeaveRequest(LeaveRequestDto dto) {
        idValidation(dto.getId());
        createLeaveRequest(dto);
        validateLeaveStatus(dto.getLeaveStatus());
        if (dto.getEmployeeID() == null) {
            throw new CustomValidationsException(messageService.messageResponse("employee.id.null"));
        }
    }

    public void createLeaveRequest(LeaveRequestDto dto) {
        if (dto.getReason() == null || dto.getReason().isBlank()) {
            throw new CustomValidationsException(messageService.messageResponse("leave.reason.null"));
        }
        if (dto.getReason().length() > 200) {
            throw new CustomValidationsException(messageService.messageResponse("leave.reason.to.much.length"));
        }
        dateValidation(dto.getFromDate());
        dateValidation(dto.getEndDate());
        if (dto.getLeaveDayType() == null) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NULL));
        }
        if (leaveDayTypeValidation(dto.getLeaveDayType())) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NOT_MATCHED));
        }
        if (dto.getLeaveType() == null) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NULL));
        }
        if (leaveTypeValidation(dto.getLeaveType())) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NOT_MATCHED));
        }
        LocalDate startDate = DateUtil.parseLocalDate(dto.getFromDate());
        LocalDate endDate = DateUtil.parseLocalDate(dto.getEndDate());
        assert startDate != null;
        if (!startDate.equals(endDate) && dto.getStartDateStatus() != null && !dto.getStartDateStatus().equalsIgnoreCase(LeaveDayType.SECOND_HALF.toString())) {
            throw new CustomValidationsException(messageService.messageResponse("leave.start.date.status.not.valid"));
        }
        if (!startDate.equals(endDate) && dto.getEndDateStatus() != null && !dto.getEndDateStatus().equalsIgnoreCase(LeaveDayType.FIRST_HALF.toString())) {
            throw new CustomValidationsException(messageService.messageResponse("leave.end.date.status.not.valid"));
        }
        if (startDate.equals(endDate) && (dto.getEndDateStatus()!=null || dto.getStartDateStatus()!=null)){
            throw new CustomValidationsException(messageService.messageResponse("leave.request.cant.start.end.status"));
        }
//        LocalDate today = LocalDate.now();
//        if (today.isBefore(DateUtil.parseLocalDate(dto.getFromDate()))) {
//            throw new CustomValidationsException(messageService.messageResponse("leave.request.date.is.before"));
//        }

    }

    private boolean leaveDayTypeValidation(String name) {
        for (LeaveDayType leaveDayType : LeaveDayType.values()) {
            if (leaveDayType.name().equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean leaveTypeValidation(String name) {
        for (LeaveType leaveType : LeaveType.values()) {
            if (leaveType.name().equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean leaveStatusValidation(String name) {
        for (LeaveStatus leaveStatus : LeaveStatus.values()) {
            if (leaveStatus.name().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public void dateValidation(String date) {
        if (date == null || date.isBlank()) {
            throw new CustomValidationsException(messageService.messageResponse("leave.date.null"));
        }
        if (DateUtil.isValidLocalDate(date)) {
            throw new CustomValidationsException(messageService.messageResponse("leave.date.not.valid"));
        }

    }

    public void validateLeaveStatus(String status) {
        if (status == null) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NULL));
        }
        if (leaveStatusValidation(status)) {
            throw new CustomValidationsException(messageService.messageResponse(LEAVE_STATUS_NOT_MATCHED));
        }
    }

    public void checkingNullValue(LeavePermissionDto dto) {
        idValidation(dto.getLeaveId());
        validateLeaveStatus(dto.getLeaveStatus());

    }

    public void idValidation(String id) {
        if (id == null) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.id.null"));
        }
    }


    public void checkGivenDateIsAvailableInWeekOFDays(String fromDate, String endDate, List<EmployeeWeekOff> weekOff) {
        LocalDate today = LocalDate.now();
        LocalDate startDate=DateUtil.parseLocalDate(fromDate);
        LocalDate toDate=DateUtil.parseLocalDate(endDate);

        assert startDate != null;
        if (startDate.equals(toDate)) {
            for (EmployeeWeekOff week : weekOff) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(week.getDay().toUpperCase());
                LocalDate firstDayOfWeekDate = today.with(TemporalAdjusters.firstInMonth(dayOfWeek));
                LocalDate givenDayOfWeekDate = firstDayOfWeekDate.plusWeeks(week.getWeekNo()-(long)IConstant.ONE);
                if (givenDayOfWeekDate.equals(startDate)){
                    throw new CustomValidationsException(messageService.messageResponse("leave.request.cant.take.weekOf.day"));
                }
            }
        }

    }
}
