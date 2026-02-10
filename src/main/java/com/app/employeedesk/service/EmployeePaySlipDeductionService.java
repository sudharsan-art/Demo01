package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeePaySlipAllowanceDto;
import com.app.employeedesk.dto.EmployeePaySlipDeductionDto;
import com.app.employeedesk.entity.EmployeePayRollAndPaySlips;
import com.app.employeedesk.entity.EmployeePaySlipAllowances;
import com.app.employeedesk.entity.EmployeePaySlipDeductions;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.repo.EmployeePaySlipDeductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeePaySlipDeductionService {

    private final EmployeePaySlipDeductionRepository employeePaySlipDeductionRepository;
    public EmployeePaySlipDeductions createPayslipDeductionWithOutId(EmployeePaySlipDeductionDto employeePaySlipAllowanceDto, Double amount, EmployeePayRollAndPaySlips employeePayRollAndPaySlips){
        return EmployeePaySlipDeductions.builder().percentage(employeePaySlipAllowanceDto.getPercentage())
                .amount(amount)
                .reason(employeePaySlipAllowanceDto.getReason())
                .employeePayRollAndPaySlips(employeePayRollAndPaySlips)
                .status(Status.ACTIVE)
                .build();
    }
    public EmployeePaySlipDeductions createPayslipDeductionWithId(EmployeePaySlipDeductionDto employeePaySlipDeductionDto, Double amount, EmployeePayRollAndPaySlips employeePayRollAndPaySlips){
        return EmployeePaySlipDeductions.builder().id(employeePaySlipDeductionDto.getDeductionId())
                .percentage(employeePaySlipDeductionDto.getPercentage())
                .amount(amount)
                .reason(employeePaySlipDeductionDto.getReason())
                .employeePayRollAndPaySlips(employeePayRollAndPaySlips)
                .status(Status.ACTIVE)
                .build();
    }

    public List<EmployeePaySlipDeductionDto> getPayslipDeductionDetails(UUID payslipId){
        return employeePaySlipDeductionRepository.getEmployeePaySlipDeductionDetails(payslipId);
    }

    public void saveAll(List<EmployeePaySlipDeductions> employeePaySlipDeductions){
        employeePaySlipDeductionRepository.saveAll(employeePaySlipDeductions);
    }


}
