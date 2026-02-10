package com.app.employeedesk.service;

import com.app.employeedesk.dto.LeaveFilterDto;
import com.app.employeedesk.dto.LeavePermissionDto;
import com.app.employeedesk.dto.LeaveRequestDto;
import com.app.employeedesk.dto.LeaveRequestListDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeWeekOff;
import com.app.employeedesk.entity.LeaveRequest;
import com.app.employeedesk.enumeration.LeaveDayType;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.LeaveType;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.LeaveRequestRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.validation.LeaveRequestValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final MessageService messageService;

    private final LeaveRequestRepository leaveRepository;

    private final EmployeePersonalDetailsService personalDetailsService;

    private final LeaveRequestValidation leaveRequestValidation;

    private final EmployeeShiftWeekOffService employeeShiftWeekOffService;


    private final EntityManager entityManager;

    //create new leave request (time=4hr)
    public String createLeaveRequest(Principal principal, LeaveRequestDto dto) {
        leaveRequestValidation.createLeaveRequest(dto);
        EmployeeBasicDetails basicDetails = personalDetailsService.getEmployeeDetailsByPrinciple(principal);
        LocalDate fromDate = DateUtil.parseLocalDate(dto.getFromDate());
        LocalDate endDate = DateUtil.parseLocalDate(dto.getEndDate());
        UUID employeeId = basicDetails.getId();
        List<EmployeeWeekOff> weekOff = employeeShiftWeekOffService.findEmployeeSiftWeekOf(employeeId);
        leaveRequestValidation.checkGivenDateIsAvailableInWeekOFDays(dto.getFromDate(), dto.getEndDate(), weekOff);
        List<LeaveStatus> statuses = Arrays.asList(LeaveStatus.ACCEPT, LeaveStatus.PENDING);

        //validate a leave request date
        boolean leaveValidation = leaveRepository.isDateBetweenFromAndEndDate(fromDate, endDate, employeeId, statuses).orElse(false);
        if (leaveValidation) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.data.already.exist"));
        }
        boolean startStatus = checkLeaveDayTypeStatus(dto.getStartDateStatus());
        boolean endStatus = checkLeaveDayTypeStatus(dto.getEndDateStatus());
        LeaveDayType startDateStatus = null;
        LeaveDayType endDateStatus = null;
        if (dto.getStartDateStatus()!=null && dto.getEndDateStatus()!=null) {
            startDateStatus = LeaveDayType.valueOf(dto.getStartDateStatus());
            endDateStatus = LeaveDayType.valueOf(dto.getEndDateStatus());
        }
        leaveRepository.save(LeaveRequest.builder()
                .leaveDayType(LeaveDayType.valueOf(dto.getLeaveDayType()))
                .leaveStatus(LeaveStatus.PENDING)
                .leaveType(LeaveType.valueOf(dto.getLeaveType()))
                .fromDate(DateUtil.parseLocalDate(dto.getFromDate()))
                .endDate(DateUtil.parseLocalDate(dto.getEndDate()))
                .startDateStatus(startStatus ? startDateStatus : null)
                .endDateStatus(endStatus ? endDateStatus : null)
                .reason(dto.getReason())
                .employeeId(basicDetails)
                .build());
        return messageService.messageResponse("leave.request.sent.successfully");
    }

    private boolean checkLeaveDayTypeStatus(String status) {
        return status != null;
    }

    //update a leave request (time=1.5hr)
    public String updateLeaveRequest(LeaveRequestDto dto) {
        leaveRequestValidation.updateLeaveRequest(dto);
        LocalDate fromDate = DateUtil.parseLocalDate(dto.getFromDate());
        LocalDate endDate = DateUtil.parseLocalDate(dto.getEndDate());
        UUID employeeId = UUID.fromString(dto.getEmployeeID());
        List<LeaveStatus> statuses = Arrays.asList(LeaveStatus.ACCEPT, LeaveStatus.PENDING);
        UUID leaveId = UUID.fromString(dto.getId());

        //validate a leave request date
        boolean leaveValidation = leaveRepository.isDateBetweenFromAndEndDateOrSpecificLeave(fromDate, endDate, employeeId, statuses, leaveId).orElse(false);
        if (leaveValidation) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.data.already.exist"));
        }
        LocalDate startDate = DateUtil.parseLocalDate(dto.getFromDate());
        EmployeeBasicDetails employee = personalDetailsService.findEmployeeById(UUID.fromString(dto.getEmployeeID()));
        List<EmployeeWeekOff> weekOff = employeeShiftWeekOffService.findEmployeeSiftWeekOf(employeeId);
        leaveRequestValidation.checkGivenDateIsAvailableInWeekOFDays(dto.getFromDate(), dto.getEndDate(), weekOff);
        boolean startStatus = checkLeaveDayTypeStatus(dto.getStartDateStatus());
        boolean endStatus = checkLeaveDayTypeStatus(dto.getEndDateStatus());
        LeaveDayType startDateStatus = null;
        LeaveDayType endDateStatus = null;
        if (dto.getStartDateStatus()!=null && dto.getEndDateStatus()!=null) {
            startDateStatus = LeaveDayType.valueOf(dto.getStartDateStatus());
            endDateStatus = LeaveDayType.valueOf(dto.getEndDateStatus());
        }
        assert startDate != null;
        if (LeaveStatus.valueOf(dto.getLeaveStatus()).equals(LeaveStatus.PENDING)) {
            leaveRepository.save(LeaveRequest.builder()
                    .id(UUID.fromString(dto.getId()))
                    .leaveDayType(LeaveDayType.valueOf(dto.getLeaveDayType()))
                    .leaveStatus(LeaveStatus.PENDING)
                    .leaveType(LeaveType.valueOf(dto.getLeaveType()))
                    .fromDate(DateUtil.parseLocalDate(dto.getFromDate()))
                    .endDate(DateUtil.parseLocalDate(dto.getEndDate()))
                    .startDateStatus(startStatus ? startDateStatus : null)
                    .endDateStatus(endStatus ? endDateStatus : null)
                    .reason(dto.getReason())
                    .employeeId(employee)
                    .build());
            return messageService.messageResponse("leave.request.update.successfully");
        }
        throw new CustomValidationsException(messageService.messageResponse("leave.can.not.modify"));
    }

    //get all leave request for specify employee (time=1hr)
    public List<LeaveRequestDto> getAllLeaveRequest(Principal principal) {
        EmployeeBasicDetails basicDetails = personalDetailsService.getEmployeeDetailsByPrinciple(principal);
        UUID employeeId = basicDetails.getId();
        List<LeaveRequest> leaveRequests = leaveRepository.findByEmployeeId(employeeId).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("leave.data.not.found")));
        List<LeaveRequestDto> requestDto = new ArrayList<>();

        leaveRequests.forEach(i -> {
            String startDate = DateUtil.parseString(i.getFromDate());
            String endDate = DateUtil.parseString(i.getEndDate());
            requestDto.add(LeaveRequestDto.builder()
                    .id(String.valueOf(i.getId()))
                    .leaveStatus(String.valueOf(i.getLeaveStatus()))
                    .reason(i.getReason())
                    .leaveType(String.valueOf(i.getLeaveType()))
                    .fromDate(startDate)
                    .endDate(endDate)
                    .leaveDayType(String.valueOf(i.getLeaveDayType()))
                    .employeeID(String.valueOf(i.getEmployeeId())).build());
        });


        return requestDto;
    }

    //delete specify leave request (time=45min)
    public String delete(String requestId) {

        if (requestId == null || requestId.isBlank()) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.id.null"));
        }
        LeaveRequest leaveRequest = leaveRepository.getReferenceById(UUID.fromString(requestId));
        if (!leaveRequest.getLeaveStatus().equals(LeaveStatus.ACCEPT)) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.cant.delete"));
        }
        leaveRepository.deleteById(UUID.fromString(requestId));
        return messageService.messageResponse("leave.delete.successfully");
    }

    //this is filter api for admin to fetch specify criteria (time=2hr)
    public List<LeaveRequestListDto> requestFilter(LeaveFilterDto dto) {
        leaveRequestValidation.dateValidation(dto.getDate());
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LeaveRequestListDto> cq = cb.createQuery(LeaveRequestListDto.class);
        Root<LeaveRequest> rootLeave = cq.from(LeaveRequest.class);
        Join<LeaveRequest, EmployeeBasicDetails> employeeJoin = rootLeave.join("employeeId", JoinType.INNER);
        List<Predicate> predicate = new ArrayList<>();
        if (dto.getDate() != null && !dto.getDate().isBlank()) {
            predicate.add(cb.equal(rootLeave.get("fromDate"), DateUtil.parseLocalDate(dto.getDate())));
        }
        if (dto.getLeaveStatus() != null && !dto.getLeaveStatus().isBlank()) {
            predicate.add(cb.equal(rootLeave.get("leaveStatus"), LeaveStatus.valueOf(dto.getLeaveStatus())));
        }
        if (dto.getLeaveType() != null && !dto.getLeaveType().isBlank()) {
            predicate.add(cb.equal(rootLeave.get("leaveDayType"), LeaveType.valueOf(dto.getLeaveType())));
        }
        cq.select(cb.construct(LeaveRequestListDto.class,
                        rootLeave.get("id"),
                        employeeJoin.get("employeecode"),
                        cb.concat(
                                cb.concat(
                                        employeeJoin.get("firstName"),
                                        cb.concat(" ", cb.coalesce(employeeJoin.get("middleName"), ""))
                                ), cb.concat(" ", employeeJoin.get("lastName"))),
                        rootLeave.get("fromDate"),
                        rootLeave.get("endDate"),
                        rootLeave.get("leaveType")))
                .where(cb.and(predicate.toArray(new Predicate[0])));
        TypedQuery<LeaveRequestListDto> result = entityManager.createQuery(cq);
        result.setFirstResult((dto.getPageNumber() - 1) * dto.getPageSize());
        result.setMaxResults(dto.getPageSize());
        return result.getResultList();
    }

    // admin approve leave request (time=45min)
    public String approveLeaveRequest(LeavePermissionDto dto) {
        leaveRequestValidation.checkingNullValue(dto);
        LeaveRequest leaveRequest = leaveRepository.findById(UUID.fromString(dto.getLeaveId())).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("leave.request.not.available")));
        boolean approvalValidation = leaveRepository.approvalValidation(leaveRequest.getEmployeeId().getId(), leaveRequest.getFromDate(), leaveRequest.getEndDate()).orElse(false);
        if (approvalValidation && LeaveStatus.ACCEPT.equals(LeaveStatus.valueOf(dto.getLeaveStatus()))) {
            throw new CustomValidationsException(messageService.messageResponse("leave.request.cant.modify"));
        }

        leaveRequest.setLeaveStatus(LeaveStatus.valueOf(dto.getLeaveStatus()));
        leaveRepository.save(leaveRequest);
        return messageService.messageResponse("leave.request.update.successfully");
    }

    //get all employee pending leave request (time=15min)
    public List<LeaveRequestListDto> getAllPendingEmployee() {
        return leaveRepository.getAllPendingRequest().orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("leave.request.no.data.found")));
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void allLeaveRequestAreShouldBeAbsent() {
        LocalDate today = LocalDate.now();
        LeaveStatus status = LeaveStatus.PENDING;
        List<LeaveRequest> allRequest = leaveRepository.todayPendingRequest(status, today).orElse(null);
        if (allRequest != null) {
            allRequest.forEach(i -> i.setLeaveStatus(LeaveStatus.REJECT));
            leaveRepository.saveAll(allRequest);
        }
    }

    public void checkLeaveRequest(){
        LocalDate date=LocalDate.now();
        boolean isCheckTodayLeaveOrNot=leaveRepository.checkDate(date).orElse(false);
        if (isCheckTodayLeaveOrNot){
            throw new CustomValidationsException(messageService.messageResponse("no.check.because.leave.status.accept"));
        }
    }

}
