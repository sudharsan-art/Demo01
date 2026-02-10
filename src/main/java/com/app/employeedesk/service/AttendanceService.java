package com.app.employeedesk.service;

import com.app.employeedesk.bean.EmployeeAttendance;
import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.entity.AttendanceTiming;
import com.app.employeedesk.entity.AttendanceUpdate;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.enumeration.AttendanceStatus;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.UpdateStatus;
import com.app.employeedesk.enumeration.WorkMode;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;

import com.app.employeedesk.repo.AttendanceRepository;
import com.app.employeedesk.repo.AttendanceTimingRepository;
import com.app.employeedesk.repo.AttendanceUpdateRepository;
import com.app.employeedesk.repo.EmployeeMonthlyPaySlipRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.validation.AttendanceReportSearchValidations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MessageService messageService;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final HolidayMasterService holidayMasterService;
    private final AttendanceTimingService timingService;
    private final LeaveRequestService leaveRequestService;
    private final AttendanceUpdateRepository attendanceUpdateRepository;
    private final AttendanceTimingRepository repository;
    private final AttendanceReportSearchValidations attendanceReportSearchValidations;
    private final EmployeeMonthlyPaySlipRepository employeeMonthlyPaySlipRepository;
    private final AttendanceTimingRepository attendanceTimingRepository;


    @Transactional
    public String checkIn(String workMode, Principal principal) {
        EmployeeBasicDetails employee = employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        UUID empId = employee.getId();
        LocalDate date = LocalDate.now();
        Attendance attendance = attendanceRepository.findByDateAndEmpId(date, empId).orElse(null);
        if (attendance == null) {
            leaveRequestService.checkLeaveRequest();
            Attendance newAttendance = attendanceRepository.save(Attendance.builder()
                    .date(date)
                    .workMode(WorkMode.valueOf(workMode))
                    .employeeId(empId)
                    .build());
            timingService.saveCheckIn(newAttendance);
        } else {
            AttendanceTiming attendanceTiming=timingService.findByCheckInNullOrNot(attendance.getId());
            if(attendanceTiming==null) {
                timingService.saveCheckIn(attendance);
            }
            else {
                throw new CustomValidationsException(messageService.messageResponse("Attendance.check.out.here.so.u.cant.check.in"));
            }
        }
        return messageService.messageResponse("Attendance.checkin.success");
    }

    public String checkOut(Principal principal) {
        EmployeeBasicDetails employee = employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        UUID empId = employee.getId();
        LocalDate date = LocalDate.now();
        Attendance attendance = attendanceRepository.findByDateAndEmpId(date, empId).orElse(null);
        if (attendance == null) {
            throw new CustomValidationsException(messageService.messageResponse("Attendance.not.available"));
        }
        timingService.findValidCheckOut(attendance.getId());

        return messageService.messageResponse("Attendance.checkout.success");
    }

    @Scheduled(cron ="  0 0 11 * * ?")
    public void changeStatusHolidayOrPresentOrAbsent() {
        LocalDate today = LocalDate.now();
        List<Attendance> attendances = attendanceRepository.findByDate(today);
        List<UUID> employeeId = employeePersonalDetailsService.getAllEmployeeId();
        boolean isCheck = holidayMasterService.checkHolidayOrNot(today);
        if(!attendances.isEmpty() && isCheck){
            for(Attendance i:attendances){
                EmployeeWorkingTime employeeWorkingTime=timingService.calcuateWorkingTime(i);
                if(employeeWorkingTime.getHours()>4){
                    i.setStatus(AttendanceStatus.HOLIDAY_BUT_PRESENT);
                    i.setHours(employeeWorkingTime.getHours());
                    i.setMinutes(employeeWorkingTime.getMinutes());
                }



            }
            attendanceRepository.saveAll(attendances);


        }
        if (attendances.isEmpty()) {

            if (isCheck) {
                List<Attendance> allAttendance = employeeId.stream().map(empId -> holidayAttendance(empId, today)).toList();
                attendanceRepository.saveAll(allAttendance);
            } else {
                List<Attendance> allAttendance = employeeId.stream().map(empId -> absentEmployee(empId, today)).toList();
                attendanceRepository.saveAll(allAttendance);
            }
        } else {

            attendances.forEach(attendance -> {

                EmployeeWorkingTime time = timingService.findTotalWorkingHours(attendance.getId(),attendance.getEmployeeId(),today);
                if (time!=null) {
                    attendance.setStatus(AttendanceStatus.PRESENT);
                    attendance.setHours(time.getHours());
                    attendance.setMinutes(time.getMinutes());
                }
                else {
                    checkOutTime(attendance);
                }
            });
            attendanceRepository.saveAll(attendances);
            List<Attendance> absentEmployee = new ArrayList<>();
            employeeId.forEach(findAbsentEmployee -> findEmployeeId(findAbsentEmployee, attendances, absentEmployee, today));
            attendanceRepository.saveAll(absentEmployee);
        }
    }

    private Attendance absentEmployee(UUID empId, LocalDate today) {
        return Attendance.builder().status(AttendanceStatus.ABSENT)
                .employeeId(empId)
                .date(today)
                .build();
    }

    private Attendance holidayAttendance(UUID id, LocalDate date) {
        return Attendance.builder().status(AttendanceStatus.HOLIDAY)
                .employeeId(id)
                .date(date)
                .build();
    }

    private void findEmployeeId(UUID findAbsentEmployee, List<Attendance> attendances, List<Attendance> absentEmployee, LocalDate today) {
        boolean checkEmployee = attendances.stream().anyMatch(attendance -> attendance.getEmployeeId().equals(findAbsentEmployee));
        if (!checkEmployee) {
            absentEmployee.add(Attendance.builder().employeeId(findAbsentEmployee).status(AttendanceStatus.ABSENT).date(today).build());
        }
    }

    private void checkOutTime(Attendance attendances) {
        List<AttendanceTiming> attendanceTiming=repository.findByTotalhours(attendances.getId());
        if (attendanceTiming.size()==1&&attendanceTiming.get(0).getOutTime()==null){
            attendances.setHours(0);
            attendances.setMinutes(0L);
            attendances.setStatus(AttendanceStatus.ABSENT);
        }



    }
    public FindEmployeeNameDto searchEmployee(String employeeId){
        return employeePersonalDetailsService. getEmployeeId(employeeId);
    }
    public String updateAttendance(UpdateAttendanceDto dto){
        Attendance attendance=attendanceRepository.findByEmployeeIdAndDate(dto.getEmpId(), DateUtil.parseLocalTime(dto.getDate()));
        attendance.setStatus(AttendanceStatus.valueOf(dto.getStatus()));
        attendance.setOutTime(DateUtil.parseLocalTime(dto.getOutTime()));
        attendance.setInTime(DateUtil.parseLocalTime(dto.getInTime()));
        return messageService.messageResponse("Attendance.update.successfully");
    }


      public List<EmployeeAttendanceDto> getEmployeeAttendanceDetails(AttendanceFilterDto attendanceFilterDto){
        return attendanceRepository.getEmployeeAttendanceDetails(attendanceFilterDto.getYear(),attendanceFilterDto.getMonth());
    }

    public Map<FindEmployeeNameDto,Long> getAllEmployeeNameCode(AttendanceFilterDto attendanceFilterDto){
        Map<FindEmployeeNameDto,Long> map=new HashMap<>();
        List<EmployeeAttendanceDto> attendanceDto=getEmployeeAttendanceDetails(attendanceFilterDto);
        for(EmployeeAttendanceDto employeeAttendanceDto:attendanceDto){
            map.put(searchEmployee(String.valueOf(employeeAttendanceDto.getEmployeeId())),employeeAttendanceDto.getTotalWorkingHrs());
        }
        return map;
    }
    public Object getInTimeOutTime(String dateStr,Principal principal){
        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        try{
            date=LocalDate.parse(dateStr,formatter);
        }catch (Exception e){
            throw new CustomValidationsException("invalid date");
        }
        if(attendanceRepository.checkEmployeeAbsentOrHoliday(date,employeeBasicDetails.getId())){
            throw new CustomValidationsException("you.may.leave.or.absent");
        }

        return attendanceTimingRepository.findInTimeOutTime(date,employeeBasicDetails.getId()).orElse(null);

    }

    public String  employeeAttendanceUpdate(EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto,Principal principal) throws ParseException {
        List<String> errorMessage=new ArrayList<>();
        AttendanceUpdate attendanceUpdate=new AttendanceUpdate();
        attendanceReportSearchValidations.attendanceUpdateValidation(attendanceUpdate,employeeAttendanceUpdateDto,errorMessage);
        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        System.out.println(employeeBasicDetails.getId()+"   "+attendanceUpdate.getAttendanceDate());
        if(!attendanceUpdateRepository.duplicateCheck(employeeBasicDetails.getId(),attendanceUpdate.getAttendanceDate())){
            errorMessage.add(messageService.messageResponse("duplicate.entry"));
        }
        if(employeeMonthlyPaySlipRepository.employeeLastGeneratedPayslip(employeeBasicDetails.getId()).isAfter(attendanceUpdate.getAttendanceDate())){
            errorMessage.add(messageService.messageResponse("pay.slip.created"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        attendanceUpdate.setEmployeeBasicDetails(employeeBasicDetails);
        attendanceUpdate.setAppliedDate(LocalDate.now());
        attendanceUpdate.setEmpCode(employeeBasicDetails.getEmployeecode());
        attendanceUpdate.setAppliedDate(LocalDate.now());
        attendanceUpdateRepository.save(attendanceUpdate);
        return messageService.messageResponse("saved.success");
    }

    public Object getRecordsAdminAproval(AttendanceUpdateAdminFilter attendanceUpdateAdminFilter){

        Integer month=0;
        Integer year=0;
        try{
            if (attendanceUpdateAdminFilter.getMonth() != null)
                month = Integer.parseInt(attendanceUpdateAdminFilter.getMonth());

            if (attendanceUpdateAdminFilter.getYear() != null)
                year = Integer.parseInt(attendanceUpdateAdminFilter.getYear());

        } catch (NumberFormatException e) {
           throw new CustomValidationsException(messageService.messageResponse(e.getMessage()));
       }
        if(!(attendanceUpdateAdminFilter.getStatus().equals("Accepted") ||
                attendanceUpdateAdminFilter.getStatus().equals("Pending") ||
                attendanceUpdateAdminFilter.getStatus().equals("Rejected") ||
                attendanceUpdateAdminFilter.getStatus().equals("All") ||
                attendanceUpdateAdminFilter.getStatus()==null)){
            throw new CustomValidationsException(messageService.messageResponse("invalid.status"));
        }
        if(attendanceUpdateAdminFilter.getStatus()== null){
            attendanceUpdateAdminFilter.setStatus("Pending");
        }
      Optional<List<AttendanceUpdate>> attendanceUpdates= attendanceUpdateRepository.fetchForAdminView(attendanceUpdateAdminFilter.getStatus(),month,year);
       if(attendanceUpdates.isEmpty() || attendanceUpdates.get().isEmpty()){
            return messageService.messageResponse("no.match.available");
        }
       return attendanceUpdates.get().stream().map(this::entityToDto).toList();
    }
    @Transactional
    public Object adminResponseStatus(EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto) throws ParseException {
        List<String> errorMessage=new ArrayList<>();
        AttendanceUpdate attendanceUpdate=new AttendanceUpdate();
        attendanceReportSearchValidations.attendanceUpdateValidation(attendanceUpdate,employeeAttendanceUpdateDto,errorMessage);
        attendanceUpdate.setId(employeeAttendanceUpdateDto.getId());
        attendanceUpdate.setAdminRemark(employeeAttendanceUpdateDto.getAdminRemark());
        attendanceUpdate.setEmpCode(employeeAttendanceUpdateDto.getEmpCode());
        attendanceUpdate.setUpdateStatus(UpdateStatus.ACCEPTED);
        attendanceUpdateRepository.save(attendanceUpdate);
        List<AttendanceTiming> attendanceTimings=attendanceRepository.getAttendanceTiming(attendanceUpdate.getAttendanceDate(),attendanceUpdate.getEmployeeBasicDetails().getId());

        if(!attendanceTimings.isEmpty()){
            if(attendanceUpdate.getUpdateField().equals("InTime")){
               Optional<AttendanceTiming> attendanceTiming= attendanceTimings.stream().min(Comparator.comparing(AttendanceTiming::getOutTime));
                attendanceTiming
                        .ifPresent(timing -> timing.setInTime(attendanceUpdate.getInTime()));
                attendanceTiming.ifPresent(attendanceTimingRepository::save);
            }
            if(attendanceUpdate.getUpdateField().equals("OutTime")){
                Optional<AttendanceTiming> attendanceTiming= attendanceTimings.stream().max(Comparator.comparing(AttendanceTiming::getInTime));
                attendanceTiming.ifPresent(timing ->timing.setOutTime(attendanceUpdate.getOutTime()));
                if(attendanceTiming.isPresent()){
                    attendanceTiming.get().setOutTime(attendanceUpdate.getOutTime());
                    attendanceTimingRepository.save(attendanceTiming.get());
                }

            }
        }else {
            if (attendanceUpdate.getUpdateField().equals("both")) {
                Attendance attendance = attendanceRepository.getAttendance(attendanceUpdate.getAppliedDate(), attendanceUpdate.getEmployeeBasicDetails().getId());

                attendanceTimingRepository.save(AttendanceTiming.builder().attendances(attendance)
                        .inTime(attendanceUpdate.getInTime())
                        .outTime(attendanceUpdate.getOutTime())
                        .build());
            }
        }
       List<EmployeeAttendance> attendanceTimings2=attendanceRepository.getAttendanceTimingByAttendance(attendanceUpdate.getEmployeeBasicDetails().getId(),attendanceUpdate.getAttendanceDate());

        Attendance attendance = attendanceTimings2.get(0).getAttendance();
        List<AttendanceTiming> attendanceTiming =new ArrayList<>();
        for(EmployeeAttendance i:attendanceTimings2){
            attendanceTiming.add(i.getAttendanceTiming());
        }
        if(attendanceTiming !=null){
            LocalTime inTime=attendanceTiming.stream().min(Comparator.comparing(AttendanceTiming::getInTime)).get().getInTime();
            LocalTime outTime=attendanceTiming.stream().max(Comparator.comparing(AttendanceTiming::getOutTime)).get().getOutTime();
            Duration duration=Duration.between(inTime,outTime);


            if(duration.toHours()>=8){
                attendance.setStatus(AttendanceStatus.PRESENT);
                attendanceRepository.save(attendance);
            }else
                if(duration.toHours()>=4){
                attendance.setStatus(AttendanceStatus.HALF_DAY);
                attendanceRepository.save(attendance);

            }else {
                attendance.setStatus(AttendanceStatus.ABSENT);
                attendanceRepository.save(attendance);
            }
        }
        return messageService.messageResponse("updated.sucessfully");

    }
    public Object rejectProcess(EmployeeAttendanceUpdateDto employeeAttendanceUpdateDto){
        Optional<AttendanceUpdate> attendanceUpdate= attendanceUpdateRepository.findById(employeeAttendanceUpdateDto.getId());
        attendanceUpdate.get().setUpdateStatus(UpdateStatus.REJECTED);
        attendanceUpdate.ifPresent(attendanceUpdateRepository::save);
        if (employeeAttendanceUpdateDto.getReason() != null && employeeAttendanceUpdateDto.getReason().length()<255){
            attendanceUpdate.ifPresent(o->o.setReason(employeeAttendanceUpdateDto.getReason()));
        }
        return messageService.messageResponse("updated.sucessfully");
    }
    @PersistenceContext
    private EntityManager entityManager;

    public List<AttendanceRequestViewDto> employeeScreenFilter(AttendanceUpdateAdminFilter attendanceUpdateAdminFilter,Principal principal){
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<AttendanceRequestViewDto> criteriaQuery=criteriaBuilder.createQuery(AttendanceRequestViewDto.class);
        Root<AttendanceUpdate> attendanceUpdateRoot=criteriaQuery.from(AttendanceUpdate.class);
        List<Predicate> predicateList=new ArrayList<>();
        if(attendanceUpdateAdminFilter.getYear()!=null && attendanceUpdateAdminFilter.getYear().length()==4){
            Expression<Integer> integerExpression=criteriaBuilder.function("YEAR",Integer.class,attendanceUpdateRoot.get("attendanceDate"));
            predicateList.add(criteriaBuilder.equal(integerExpression,attendanceUpdateAdminFilter.getYear()));
        }
        if(attendanceUpdateAdminFilter.getMonth()!=null && attendanceUpdateAdminFilter.getMonth().length()<3){
            Expression<Integer> integerExpression=criteriaBuilder.function("MONTH",Integer.class,attendanceUpdateRoot.get("attendanceDate"));
            predicateList.add(criteriaBuilder.equal(integerExpression,attendanceUpdateAdminFilter.getMonth()));
        }
        if(attendanceUpdateAdminFilter.getStatus()==null){
            predicateList.add(criteriaBuilder.equal(attendanceUpdateRoot.get("updateStatus"),UpdateStatus.PENDING));
        }
        if(attendanceUpdateAdminFilter.getStatus()!=null){
            predicateList.add(criteriaBuilder.equal(attendanceUpdateRoot.get("updateStatus"),attendanceUpdateAdminFilter.getStatus()));
        }
        EmployeeBasicDetails id=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        if(id!=null){
            predicateList.add(criteriaBuilder.equal(attendanceUpdateRoot.get("employeeBasicDetails"),id));
        }
        criteriaQuery.select(criteriaBuilder.construct(
                AttendanceRequestViewDto.class,
                attendanceUpdateRoot.get("attendanceDate"),
                attendanceUpdateRoot.get("inTime"),
                attendanceUpdateRoot.get("outTime"),
                attendanceUpdateRoot.get("reason"),
                attendanceUpdateRoot.get("updateStatus"),
                attendanceUpdateRoot.get("adminRemark")
        ));

        criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();




    }



    public EmployeeAttendanceUpdateDto entityToDto(AttendanceUpdate attendanceUpdate){
        return EmployeeAttendanceUpdateDto.builder()
                .id(attendanceUpdate.getId())
                .date(attendanceUpdate.getInTime() != null ? attendanceUpdate.getOutTime().toString():null)
                .inTime( attendanceUpdate.getInTime() != null ? attendanceUpdate.getInTime().toString():null)
                .outTime(attendanceUpdate.getOutTime().toString())
                .reason(attendanceUpdate.getReason())
                .employeeName(attendanceUpdate.getEmployeeBasicDetails().getFirstName()+attendanceUpdate.getEmployeeBasicDetails().getLastName())
                .appliedDate(attendanceUpdate.getAppliedDate() != null ? attendanceUpdate.getAppliedDate().toString():null)
                .empCode(attendanceUpdate.getEmpCode())
                .updateStatus(String.valueOf(attendanceUpdate.getUpdateStatus()))
                .adminRemark(attendanceUpdate.getAdminRemark())
                .employeeBasicDetails(attendanceUpdate.getEmployeeBasicDetails().getId())
                .build();

    }



}
