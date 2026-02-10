package com.app.employeedesk.service;

import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.*;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.EmployeeMonthlyPaySlipRepository;
import com.app.employeedesk.response.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeMonthlyPayslipService {

    private final EmployeePayRollPaySlipService employeePayRollPaySlipService;

    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    private final EmployeeMonthlyPaySlipRepository employeeMonthlyPaySlipRepository;

    private final TemplateEngine templateEngine;

    private final MessageService messageService;

    public void generateEmployeePayslip() throws JSONException {
        List<EmployeePayRollAndPaySlips> employeePayRollAndPaySlips=employeePayRollPaySlipService.findAllPayslipDetails();
        List<EmployeeMonthlyPayslip> employeeMonthlyPayslips=new ArrayList<>();
        LocalDate generatedDate=LocalDate.now();
        for(EmployeePayRollAndPaySlips i:employeePayRollAndPaySlips){
            EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.findEmployeeById(i.getEmployeeBasicDetails().getId());
            JSONObject json=new JSONObject();
            Map<String,Double> allowanceList=new HashMap<>();
            Map<String,Object> deductionList=new HashMap<>();
            json.put("name",employeeBasicDetails.getFirstName());
            json.put("employeeCode",employeeBasicDetails.getEmployeecode());
            json.put("designation",employeeBasicDetails.getDesigination());
            json.put("department",employeeBasicDetails.getDepartment());
            json.put("basicSalary",i.getBasicSalary());
            json.put("netSalary",i.getNetSalary());
            for(EmployeePaySlipAllowances j:i.getEmployeePaySlipAllowances()){
                allowanceList.put(j.getReason(),j.getAmount());
            }
            json.put("allowanceList",allowanceList);

            for(EmployeePaySlipDeductions k:i.getEmployeePaySlipDeductions()){
                deductionList.put(k.getReason(),k.getAmount());
            }
            json.put("deductionList",deductionList);
            employeeMonthlyPayslips.add(EmployeeMonthlyPayslip.builder()
                    .employeeBasicDetails(employeeBasicDetails)
                    .payslipData(json.toString())
                    .generatedOn(generatedDate)
                    .build());
        }
        employeeMonthlyPaySlipRepository.saveAll(employeeMonthlyPayslips);
        MessageDto.builder().message(messageService.messageResponse("monthly.payslip.generated")).build();
    }

    @Scheduled(cron = "0 0 0 L * *")
    public void generatePayslipsForEveryMonth()throws JSONException {
        try {
            generateEmployeePayslip();
        } catch (JSONException e) {
            throw new JSONException(messageService.messageResponse(e.getMessage()));
        }
    }

    public List<EmployeePaySlipAllowanceDto> convertAllowancesList(String payslipJsonString){
        String[] allowanceData = payslipJsonString.substring(1, payslipJsonString.length() - 1).split(", ");
        List<EmployeePaySlipAllowanceDto> allowances = new ArrayList<>();
        for (String data : allowanceData) {
            String[] parts = data.split("=");
            allowances.add(EmployeePaySlipAllowanceDto.builder().reason(parts[0])
                    .amount(Double.parseDouble(parts[1]))
                    .build());
        }
        return allowances;
    }
    public List<EmployeePaySlipDeductionDto> convertDeducionList(String payslipJsonString){
        String[] allowanceData = payslipJsonString.substring(1, payslipJsonString.length() - 1).split(", ");
        List<EmployeePaySlipDeductionDto> deductions = new ArrayList<>();
        for (String data : allowanceData) {
            String[] parts = data.split("=");
            deductions.add(EmployeePaySlipDeductionDto.builder().reason(parts[0])
                    .amount(Double.parseDouble(parts[1]))
                    .build());
        }
        return deductions;
    }


    public  byte[] employeePayslipPdf(UUID employeeId, String date) throws IOException {
        String[] splitDate=date.split("-");
        String month=splitDate[0];
        String year=splitDate[1];
        int monthNumber = Integer.parseInt(splitDate[0]);
        String monthName = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        EmployeeMonthlyPayslipOutputDto employeeMonthlyPayslipOutputDto= employeeMonthlyPaySlipRepository.getEmployeeMonthlyPayslipDetails(employeeId,month,year).orElseThrow(
                ()->new CustomValidationsException(messageService.messageResponse("employee.payslip.empty")));
        ObjectMapper objectMapper = new ObjectMapper();
        PayslipJsonDto payslipJsonDto = objectMapper.readValue(employeeMonthlyPayslipOutputDto.getPayslipData(), PayslipJsonDto.class);
        List<EmployeePaySlipAllowanceDto> employeePaySlipAllowanceDto= convertAllowancesList(payslipJsonDto.getAllowanceList());
        List<EmployeePaySlipDeductionDto> employeePaySlipDeductionDto=convertDeducionList(payslipJsonDto.getDeductionList());
        Context context = new Context();
        context.setVariable("monthName",monthName);
        context.setVariable("basicSalary",payslipJsonDto.getBasicSalary());
        context.setVariable("netSalary",payslipJsonDto.getNetSalary());
        context.setVariable("employeeMonthlyPayslipOutputDto", employeeMonthlyPayslipOutputDto);
        context.setVariable("employeePaySlipAllowanceDto",employeePaySlipAllowanceDto);
        context.setVariable("employeePaySlipDeductionDto",employeePaySlipDeductionDto);
        String html = templateEngine.process("PayslipPdfView", context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    public String getEmployeePaySlipJson(UUID employeeId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(employeeMonthlyPaySlipRepository.getEmployeePaySlipJson(employeeId), PayslipJsonDto.class).getNetSalary();
    }
}
