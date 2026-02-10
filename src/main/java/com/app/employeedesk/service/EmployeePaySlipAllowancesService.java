package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.entity.EmployeePayRollAndPaySlips;
import com.app.employeedesk.entity.EmployeePaySlipAllowances;
import com.app.employeedesk.entity.EmployeePaySlipDeductions;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.repo.EmployeePaySlipAllowancesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeePaySlipAllowancesService {

    private final EmployeePaySlipAllowancesRepository employeePaySlipAllowancesRepository;

    public EmployeePaySlipAllowances createPayslipAllowanceWithOutId(EmployeePaySlipAllowanceDto employeePaySlipAllowanceDto, Double amount, EmployeePayRollAndPaySlips employeePayRollAndPaySlips){
       return EmployeePaySlipAllowances.builder().percentage(employeePaySlipAllowanceDto.getPercentage())
               .amount(amount)
                .reason(employeePaySlipAllowanceDto.getReason())
               .employeePayRollAndPaySlips(employeePayRollAndPaySlips)
                .status(Status.ACTIVE)
                .build();
    }
    public EmployeePaySlipAllowances createPayslipAllowanceWithId(EmployeePaySlipAllowanceDto employeePaySlipAllowanceDto, Double amount, EmployeePayRollAndPaySlips employeePayRollAndPaySlips){
        return EmployeePaySlipAllowances.builder().id(employeePaySlipAllowanceDto.getAllowanceId())
                .percentage(employeePaySlipAllowanceDto.getPercentage())
                .amount(amount)
                .reason(employeePaySlipAllowanceDto.getReason())
                .employeePayRollAndPaySlips(employeePayRollAndPaySlips)
                .status(Status.ACTIVE)
                .build();
    }

    public void saveAll(List<EmployeePaySlipAllowances> employeePaySlipAllowancesList){
        employeePaySlipAllowancesRepository.saveAll(employeePaySlipAllowancesList);
    }

    public List<EmployeePaySlipAllowanceDto> getPayslipAllowanceDetails(UUID payslipId){
        return employeePaySlipAllowancesRepository.getEmployeePaySlipAllowanceDetails(payslipId);
    }
}
