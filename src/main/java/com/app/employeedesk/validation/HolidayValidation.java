package com.app.employeedesk.validation;

import com.app.employeedesk.dto.HolidayDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayValidation {
    private final MessageService messageService;
    public void saveValidate(HolidayDto dto) {
        if (dto.getDate() == null) {
            throw new CustomValidationsException(messageService.messageResponse("Holiday.date.null"));
        }
        if (DateUtil.isValidLocalDate(dto.getDate())) {
            throw new CustomValidationsException(messageService.messageResponse("Holiday.date.format"));
        }
        if (dto.getReason() == null) {
            throw new CustomValidationsException(messageService.messageResponse("Holiday.invalid.reason"));
        }
        if (dto.getReason().length() > 150) {
            throw new CustomValidationsException(messageService.messageResponse("Holiday.reason.length"));
        }
    }
}
