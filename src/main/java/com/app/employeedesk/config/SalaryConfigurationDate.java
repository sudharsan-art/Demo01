package com.app.employeedesk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

@Component
public class SalaryConfigurationDate {
    @Value("${salary.end.date}")
    private int salaryEndDate;

    @Value("${salary.start.date}")
    private int salaryStartDate;
    int year = Year.now().getValue();
    int month = YearMonth.now().getMonthValue();
    public int getStartDate(){
        return salaryStartDate;
    }
    public int getEndDate(){
        return salaryEndDate;
    }

    public LocalDate getSalaryEndDate(){
        return LocalDate.of(year,month,salaryEndDate);
    }

    public LocalDate getSalaryStartDate(){
        return LocalDate.of(year,month,salaryStartDate);
    }


}
