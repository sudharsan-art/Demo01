package com.app.employeedesk.validation;

import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.dto.EmployeePaySlipDeductionDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.IConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PayRollPaySlipValidation {

    private final MessageService messageService;

    Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!]");


    public void basicSalaryValidation(Double basicSalary) throws CustomValidationsException {
        String salary = Double.toString(basicSalary);
        if (salary.isBlank()) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.salary.empty"));
        }
        if (!(salary.matches("\\d+") || salary.matches("(\\d+)\\.(\\d+)")) || salary.length() > 7) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.salary.invalid"));
        }
    }

    public void payslipAllowanceDeductionValidation(Double percentage, String reason) {
        String value = Double.toString(percentage);
        if (value.isBlank()) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.percentage.empty"));
        }
        if (!(value.matches("\\d+") || value.matches("(\\d+)\\.(\\d+)")) || value.length() > 7) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.percentage.invalid"));
        }
        if (reason.length() > 50) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.reason.length"));
        }
        if (regex.matcher(reason).find()) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.reason.symbols"));
        }
    }


}
