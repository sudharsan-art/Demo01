package com.app.employeedesk.service;

import com.app.employeedesk.config.SalaryConfigurationDate;
import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.enumeration.AttendanceStatus;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeePayRollPaySlipRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.IConstant;
import com.app.employeedesk.validation.PayRollPaySlipValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeePayRollPaySlipService {

    private final EmployeePayRollPaySlipRepository employeePayRollPaySlipRepository;

    private final EmployeePaySlipAllowancesService employeePaySlipAllowancesService;

    private final EmployeePaySlipDeductionService employeePaySlipDeductionService;

    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    private final PayRollPaySlipValidation payRollPaySlipValidation;

    private final EmployeeAttendanceReportService employeeAttendanceReportService;

    private final MessageService messageService;

    private final SalaryConfigurationDate day;


    public MessageDto createUpdatePayslip(EmployeePayRollPaySlipDto employeePayRollPaySlipDto) {
        if (employeePayRollPaySlipDto.getBasicSalary() == null) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.salary.empty"));
        }
        payRollPaySlipValidation.basicSalaryValidation(employeePayRollPaySlipDto.getBasicSalary());
        EmployeeBasicDetails employeeBasicDetails = employeePersonalDetailsService.findEmployeeById(employeePayRollPaySlipDto.getEmployeeId());
        double allowanceAmount=0;
        for(EmployeePaySlipAllowanceDto i:employeePayRollPaySlipDto.getEmployeePaySlipAllowanceDto()){
            allowanceAmount+=findPercentageAmount(i.getPercentage(),employeePayRollPaySlipDto.getBasicSalary());
        }
        double deductionAmount =0;
        for(EmployeePaySlipDeductionDto j:employeePayRollPaySlipDto.getEmployeePaySlipDeductionDto()){
            deductionAmount+=findPercentageAmount(j.getPercentage(),employeePayRollPaySlipDto.getBasicSalary());
        }
        EmployeePayRollAndPaySlips employeePayRollAndPaySlips;
        if(employeePayRollPaySlipDto.getPayslipId()!=null){
            employeePayRollAndPaySlips=createPayslipWithId(employeePayRollPaySlipDto,allowanceAmount,deductionAmount,employeeBasicDetails,findNetSalary(employeePayRollPaySlipDto.getBasicSalary(),employeePayRollPaySlipDto.getEmployeeId()));
        }
        else {
            employeePayRollAndPaySlips=createPayslipWithOutId(employeePayRollPaySlipDto,allowanceAmount,deductionAmount,employeeBasicDetails,findNetSalary(employeePayRollPaySlipDto.getBasicSalary(),employeePayRollPaySlipDto.getEmployeeId()));
        }

        List<EmployeePaySlipAllowances> employeePaySlipAllowancesList = employeePayRollPaySlipDto.getEmployeePaySlipAllowanceDto().stream()
                .map(i -> {
                    basicInputValidation(i.getPercentage(), i.getReason());
                    payRollPaySlipValidation.payslipAllowanceDeductionValidation(i.getPercentage(), i.getReason());
                    double amount = findPercentageAmount(employeePayRollPaySlipDto.getBasicSalary(), i.getPercentage());
                    if(i.getAllowanceId()!=null){
                        return employeePaySlipAllowancesService.createPayslipAllowanceWithId(i, amount, employeePayRollAndPaySlips);
                    }
                    else{
                        return employeePaySlipAllowancesService.createPayslipAllowanceWithOutId(i, amount, employeePayRollAndPaySlips);
                    }
                })
                .toList();
        List<EmployeePaySlipDeductions> employeePaySlipDeductionsList = employeePayRollPaySlipDto.getEmployeePaySlipDeductionDto().stream()
                .map(j -> {
                    basicInputValidation(j.getPercentage(), j.getReason());
                    payRollPaySlipValidation.payslipAllowanceDeductionValidation(j.getPercentage(), j.getReason());
                    double amount = findPercentageAmount(employeePayRollPaySlipDto.getBasicSalary(), j.getPercentage());
                    if(j.getDeductionId()!=null){
                        return employeePaySlipDeductionService.createPayslipDeductionWithId(j, amount, employeePayRollAndPaySlips);
                    }
                    else{
                        return employeePaySlipDeductionService.createPayslipDeductionWithOutId(j, amount, employeePayRollAndPaySlips);
                    }
                })
                .toList();
        employeePayRollAndPaySlips.setEmployeePaySlipAllowances(employeePaySlipAllowancesList);
        employeePayRollAndPaySlips.setEmployeePaySlipDeductions(employeePaySlipDeductionsList);
        employeePayRollPaySlipRepository.save(employeePayRollAndPaySlips);
        return MessageDto.builder().message(messageService.messageResponse("payslip.details.create")).build();
    }



    public double findNetSalary(Double basicSalary,UUID empId){
        int fullDayCount=IConstant.ZERO;
        int halfDayCount=IConstant.ZERO;
        LocalDate now = LocalDate.now();
        LocalDate[] localDates=employeeAttendanceReportService.findStartDateAndEndDate(now.getYear(),now.getMonth());
        long betweenDays=ChronoUnit.DAYS.between(localDates[0],localDates[1])+IConstant.ONE;
        double fullDaySalary=basicSalary/betweenDays;
        double halfDaySalary=fullDaySalary/IConstant.TWO;
        for (AttendanceFilterResponseDTO i: employeeAttendanceReportService.attendanceReportFilter(now.getYear(),now.getMonth(),empId)){
            if(i.getTotalWorkingHours()>6 ||i.getStatus().equalsIgnoreCase(AttendanceStatus.HOLIDAY.toString())){
                fullDayCount++;
            }
            else if(!i.getStatus().equalsIgnoreCase(AttendanceStatus.ABSENT.toString()))
                halfDayCount++;
        }
        return (fullDayCount*fullDaySalary)+(halfDayCount*halfDaySalary);
    }

    private EmployeePayRollAndPaySlips createPayslipWithOutId(EmployeePayRollPaySlipDto employeePayRollPaySlipDto,double allowanceAmount, double deductionAmount,EmployeeBasicDetails employeeBasicDetails,double netSalary){
     return EmployeePayRollAndPaySlips.builder().basicSalary(employeePayRollPaySlipDto.getBasicSalary())
                .netSalary( (netSalary+ allowanceAmount) - deductionAmount)
                .employeeBasicDetails(employeeBasicDetails)
                .status(Status.ACTIVE)
                .build();
    }
    private EmployeePayRollAndPaySlips createPayslipWithId(EmployeePayRollPaySlipDto employeePayRollPaySlipDto,double allowanceAmount, double deductionAmount,EmployeeBasicDetails employeeBasicDetails,double netSalary){
        return EmployeePayRollAndPaySlips.builder().id(employeePayRollPaySlipDto.getPayslipId())
                .basicSalary(employeePayRollPaySlipDto.getBasicSalary())
                .netSalary( (netSalary+ allowanceAmount) - deductionAmount)
                .employeeBasicDetails(employeeBasicDetails)
                .status(Status.ACTIVE)
                .build();
    }


    public EmployeePayRollPaySlipDto getEmployeePaySlipDetails(UUID employeeId){
        EmployeePayRollPaySlipDto employeePayRollPaySlipDto= employeePayRollPaySlipRepository.getEmployeePaySlipDetails(employeeId).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("payslip.details.empty")));
        employeePayRollPaySlipDto.setEmployeePaySlipAllowanceDto(employeePaySlipAllowancesService.getPayslipAllowanceDetails(employeePayRollPaySlipDto.getPayslipId()));
        employeePayRollPaySlipDto.setEmployeePaySlipDeductionDto(employeePaySlipDeductionService.getPayslipDeductionDetails(employeePayRollPaySlipDto.getPayslipId()));
        return employeePayRollPaySlipDto;
    }

    public List<EmployeePayRollAndPaySlips> findAllPayslipDetails(){
        return employeePayRollPaySlipRepository.findAll();
    }

    public void basicInputValidation(Double percentage, String reason) throws CustomValidationsException {
        if (percentage == null) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.percentage.empty"));
        }
        if (stringCheck(reason)) {
            throw new CustomValidationsException(messageService.messageResponse("payslip.reason.empty"));
        }
    }

    public boolean stringCheck(String str) {
        return str == null || str.isBlank();
    }

    private double findPercentageAmount(Double basicSalary, Double percentage) {
        return (basicSalary * percentage) / 100;
    }

}
