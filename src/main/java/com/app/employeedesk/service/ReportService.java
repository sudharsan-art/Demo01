package com.app.employeedesk.service;

import com.app.employeedesk.dto.AttendanceFilterDto;
import com.app.employeedesk.dto.AttendanceFilterResponseDTO;
import com.app.employeedesk.dto.AttendanceFilterSearchDTO;
import com.app.employeedesk.dto.FindEmployeeNameDto;
import com.app.employeedesk.repo.EmployeeMonthlyPaySlipRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EmployeeAttendanceReportService employeeAttendanceReportService;

    private final AttendanceService attendanceService;

    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    private final EmployeeMonthlyPayslipService employeeMonthlyPayslipService;


    public byte[] generateExcelReport(AttendanceFilterSearchDTO attendanceFilterSearchDTO, Principal principal) throws IOException {
        List<AttendanceFilterResponseDTO> attendanceFilterResponseList=employeeAttendanceReportService.attendanceReportFilter(attendanceFilterSearchDTO,principal);
        try (HSSFWorkbook workBook = new HSSFWorkbook()) {
            HSSFSheet sheet = workBook.createSheet("Attendance Info");
            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Date");
            row.createCell(1).setCellValue("Status");
            row.createCell(2).setCellValue("WorkMode");
            row.createCell(3).setCellValue("Working Hours");
            int rowNumber=1;
            for(AttendanceFilterResponseDTO i:attendanceFilterResponseList){
                HSSFRow dataRow=sheet.createRow(rowNumber);
                dataRow.createCell(0).setCellValue(String.valueOf(i.getDate()));
                dataRow.createCell(1).setCellValue(i.getStatus());
                dataRow.createCell(2).setCellValue(i.getWorkMode());
                dataRow.createCell(3).setCellValue(i.getTotalWorkingHours());
                rowNumber++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workBook.write(bos);
            return bos.toByteArray();
        }
    }

    public byte[] generateEmployeeMonthlyReport(AttendanceFilterDto attendanceFilterDto) throws IOException {
        Map<FindEmployeeNameDto,Long> map=attendanceService.getAllEmployeeNameCode(attendanceFilterDto);
        try (HSSFWorkbook workBook = new HSSFWorkbook()) {
            HSSFSheet sheet = workBook.createSheet("Employees Attendance Info");
            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Employee code");
            row.createCell(1).setCellValue("Employee name");
            row.createCell(2).setCellValue("Total working hrs");
            row.createCell(3).setCellValue("Salary");
            int rowNumber = 1;
            for (Map.Entry<FindEmployeeNameDto, Long> i : map.entrySet()) {
                FindEmployeeNameDto findEmployeeNameDto=i.getKey();
                HSSFRow dataRow = sheet.createRow(rowNumber);
                dataRow.createCell(0).setCellValue(findEmployeeNameDto.getEmployeeCode());
                dataRow.createCell(1).setCellValue(findEmployeeNameDto.getEmployeeName());
                dataRow.createCell(2).setCellValue(i.getValue());
                dataRow.createCell(3).setCellValue(employeeMonthlyPayslipService.getEmployeePaySlipJson(findEmployeeNameDto.getEmployeeId()));
                rowNumber++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workBook.write(bos);
            return bos.toByteArray();
        }
    }

}
