package com.app.employeedesk.service;


import com.amazonaws.services.s3.transfer.Copy;
import com.app.employeedesk.config.SalaryConfigurationDate;
import com.app.employeedesk.dto.AttendanceFilterResponseDTO;
import com.app.employeedesk.dto.AttendanceFilterSearchDTO;

import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.AttendanceRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.IConstant;
import com.app.employeedesk.validation.AttendanceReportSearchValidations;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.lowagie.text.pdf.PdfCopy;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmployeeAttendanceReportService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceReportSearchValidations attendanceReportSearchValidations;
    private final SalaryConfigurationDate day;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;

    private final TemplateEngine templateEngine;

    private final MessageService messageService;

    public static LocalDate setStartDate(Integer year, Integer months, Integer day) {
        return LocalDate.of(year, months, 1);
    }

    public static LocalDate setendDate(Integer year, Integer months, Integer day) {
        if (months == 2 && day > 27) {
            if (year % 4 == 0) {
                return LocalDate.of(year, months, 29);
            } else {
                return LocalDate.of(year, months, 28);
            }

        }

        Integer days = LocalDate.of(year, months, 1).getDayOfMonth();
        return LocalDate.of(year, months, days);


    }

    public List<AttendanceFilterResponseDTO> attendanceReportFilter(AttendanceFilterSearchDTO attendanceFilterSearchDTO, Principal principal) {
        List<String> errorMessage = new ArrayList<>();
        attendanceReportSearchValidations.attendanceFilterValidate(attendanceFilterSearchDTO, errorMessage);
        if (!errorMessage.isEmpty()) {
            throw new ListOfValidationException(errorMessage);
        }
        LocalDate startdate;
        LocalDate endDatte;
        Integer a;
        try {

            if (day.getStartDate() != 1) {
                a = attendanceFilterSearchDTO.getMonthA() - 1;
            } else {
                a = attendanceFilterSearchDTO.getMonthA();
            }
            startdate = LocalDate.of(attendanceFilterSearchDTO.getYearA(), a, day.getStartDate());
        } catch (Exception e) {
            startdate = setStartDate(attendanceFilterSearchDTO.getYearA(), attendanceFilterSearchDTO.getMonthA(), day.getStartDate());

        }
        try {
            endDatte = LocalDate.of(attendanceFilterSearchDTO.getYearA(), attendanceFilterSearchDTO.getMonthA(), day.getEndDate());
        } catch (Exception e) {
            endDatte = setendDate(attendanceFilterSearchDTO.getYearA(), attendanceFilterSearchDTO.getMonthA(), day.getEndDate());
        }


        UUID empId =employeePersonalDetailsService.getEmployeeDetailsByPrinciples(principal);
//        Pageable pageable= PageRequest.of(attendanceFilterSearchDTO.getPageNo(),attendanceFilterSearchDTO.getPageSize());
        return attendanceRepository.attendanceReportFilter(startdate,endDatte,empId);
        // The below written query is simple query without doing this much complex operations
//        select * from attendance where (  year(date)=2008 and month(date)=10 and day(date)>=36) or (year(date)=2008 and month(date)=11 and day(date)<=20);




        // this for learning the navive query
//         String empId =employeePersonalDetailsService.getEmployeeDetailsByPrinciples(principal).toString();
//        Pageable pageable= PageRequest.of(attendanceFilterSearchDTO.getPageNo(),attendanceFilterSearchDTO.getPageSize());
//        List<EmployeeAttendanceFilterBean> AttendanceFilterResponseDTO1=attendanceRepository.attendanceReportFilter(startdate,endDatte,empId,pageable);
//        List<AttendanceFilterResponseDTO> attendanceFilterResponseDTOS=new ArrayList<>();
//        for(EmployeeAttendanceFilterBean i:AttendanceFilterResponseDTO1){
//            attendanceFilterResponseDTOS.add(AttendanceFilterResponseDTO.builder()
//                    .date(i.getDate())
//                    .workMode(i.getWorkMode())
//                    .status(i.getStatus())
//                    .totalWorkingHours(i.getHours()).build());
//        }
//        return attendanceFilterResponseDTOS;


//     List<AttendanceFilterResponseDTO> attendanceFilterResponseDTOS= attendanceRepository.attendanceReportFilter(startdate,endDatte, pageable);
//        AtttendanceFIlterFinalResponseDTO atttendanceFIlterFinalResponseDTO = new AtttendanceFIlterFinalResponseDTO();
//        atttendanceFIlterFinalResponseDTO.setNoOfDaysAbsent(attendanceFilterResponseDTOS.stream().filter(o->o.getStatus().equalsIgnoreCase("ABSENT")).count());
//        atttendanceFIlterFinalResponseDTO.setNoOfDaysPresent(attendanceFilterResponseDTOS.stream().filter(o->o.getStatus().equalsIgnoreCase("PRESENT")).count());
//        atttendanceFIlterFinalResponseDTO.setAttendanceFilterResponseDTOList(attendanceFilterResponseDTOS1);
//        return atttendanceFIlterFinalResponseDTO;

    }


    public Object attendanceReportGenerator(HttpServletResponse response, AttendanceFilterSearchDTO attendanceFilterSearchDTO, Principal principal) throws IOException {

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"filteredData.pdf\"");
    List<AttendanceFilterResponseDTO> attendances=attendanceReportFilter(attendanceFilterSearchDTO , principal );
        Document document=new Document(PageSize.A4);
        PdfCopy copy = new PdfCopy(document, response.getOutputStream());
        document.open();
        Integer pageSize=3;
        int totalPage=(int)Math.ceil((double)attendances.size()/pageSize);
        for(int page=0;page<totalPage;page++) {
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, attendances.size());
            String htmlContent = generateHtmlContext(attendances.subList(startIndex, endIndex));
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream);
            renderer.finishPDF();

            PdfReader reader = new PdfReader(byteArrayOutputStream.toByteArray());
            for(int i=1;i<reader.getNumberOfPages();i++){
                copy.addPage(copy.getImportedPage(reader,i));
            }
            reader.close();
        }
        document.close();


        return null;
    }

    public String generateHtmlContext(List<AttendanceFilterResponseDTO> dtoList) {

        Context context = new Context();
        context.setVariable("attendanceList", dtoList);
        return templateEngine.process("MonthlyAttendanceReport", context);
    }


    public List<AttendanceFilterResponseDTO> attendanceReportFilter(int year, Month month, UUID empId) {
        LocalDate[] localDates=findStartDateAndEndDate(year,month);
      //  Pageable pageable = PageRequest.of(IConstant.ZERO, 40);
        return attendanceRepository.attendanceReportFilter(localDates[0], localDates[1], empId);
    }

    public LocalDate[] findStartDateAndEndDate(int year,Month month){
        LocalDate[] localDates=new LocalDate[2];
        LocalDate toDay = LocalDate.of(year, month, IConstant.FOUR);
        LocalDate start1 = toDay.minusMonths(IConstant.ONE);
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.of(start1.getYear(), start1.getMonth(), day.getStartDate());
            endDate = LocalDate.of(toDay.getYear(), toDay.getMonth(), day.getEndDate());
        } catch (DateTimeException e) {
            throw new DateTimeException(messageService.messageResponse("date.not.found"));
        }
        localDates[0]=startDate;
        localDates[1]=endDate;
        return localDates;

    }


}
