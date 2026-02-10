package com.app.employeedesk.service;

import com.app.employeedesk.bean.WeekOfBean;
import com.app.employeedesk.dto.*;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.LeaveRequest;
import com.app.employeedesk.entity.Permission;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.PermissionRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.DateUtil;
import com.app.employeedesk.validation.PermissionValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final MessageService messageService;

    private final PermissionValidation permissionValidation;

    private final EmployeePersonalDetailsService personalDetailsService;

    private final PermissionRepository permissionRepository;

    private final EntityManager entityManager;

    // create permission
    public String createPermission(PermissionDto dto, Principal principal) {
        permissionValidation.createValidation(dto);
        EmployeeBasicDetails employee = personalDetailsService.getEmployeeDetailsByPrinciple(principal);
        SiftAndWeekOfDetailsDto detailsDto = personalDetailsService.findSiftAndWeekDetails(employee.getId());
        List<WeekOfBean> weekOfBean = permissionRepository.findId(String.valueOf(detailsDto.getSiftId()));
        List<WeekOfDto> weekOfDto = beanToDto(weekOfBean);
        permissionValidation.checkGivenDateIsAvailableInWeekOFDays(dto.getDate(), weekOfDto);
        permissionValidation.startAndEndTimeValidation(dto.getEndTime(), dto.getStartTime(), detailsDto.getSiftStartTime()
                , detailsDto.getSiftEndTime());
        LeaveRequest isPermissionValid = permissionRepository.findByAlreadyExistDate(DateUtil.parseLocalDate(dto.getDate())
                , employee.getId()).orElse(null);
        if (isPermissionValid != null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.date.already.exist.in.leave"));
        }
        Permission permission = permissionRepository.dateAlreadyExist(employee.getId(), DateUtil.parseLocalDate(dto.getDate())).orElse(null);
        if (permission != null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.date.already.exist"));
        }

        permissionRepository.save(Permission.builder().reason(dto.getReason())
                .date(DateUtil.parseLocalDate(dto.getDate()))
                .startTime(DateUtil.parseLocalTime(dto.getStartTime()))
                .endTime(DateUtil.parseLocalTime(dto.getEndTime()))
                .employeeId(employee)
                .build());
        return messageService.messageResponse("permission.sent.successfully");
    }

    public List<WeekOfDto> beanToDto(List<WeekOfBean> weekOfBean) {
        List<WeekOfDto> weekOf = new ArrayList<>();
        for (WeekOfBean week : weekOfBean) {
            weekOf.add(WeekOfDto.builder().day(week.getDays()).weekNo(week.getWeekNo()).build());
        }
        return weekOf;
    }


    public String updateUpdatePermission(PermissionDto dto) {
        permissionValidation.updateValidation(dto);
        EmployeeBasicDetails employeeBasicDetails = personalDetailsService.findEmployeeById(UUID.fromString(dto.getEmployeeId()));
        LeaveRequest isPermissionValid = permissionRepository.findByAlreadyExistDate(DateUtil.parseLocalDate(dto.getDate()), UUID.fromString(dto.getEmployeeId())).orElse(null);
        SiftAndWeekOfDetailsDto detailsDto = personalDetailsService.findSiftAndWeekDetails(UUID.fromString(dto.getId()));
        List<WeekOfBean> weekOfBean = permissionRepository.findId(String.valueOf(detailsDto.getSiftId()));
        List<WeekOfDto> weekOfDto = beanToDto(weekOfBean);
        permissionValidation.checkGivenDateIsAvailableInWeekOFDays(dto.getDate(), weekOfDto);
        if (isPermissionValid != null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.date.already.exist.in.leave"));
        }
        Permission checkPermissionDate = permissionRepository.checkPermissionDateAlreadyExistWhitOutSameId(
                UUID.fromString(dto.getId()), DateUtil.parseLocalDate(dto.getDate())).orElse(null);
        if (checkPermissionDate != null) {
            throw new CustomValidationsException(messageService.messageResponse("permission.date.already.exist"));
        }
        if (LeaveStatus.valueOf(dto.getStatus()).equals(LeaveStatus.PENDING)) {
            permissionRepository.save(Permission.builder()
                    .id(UUID.fromString(dto.getId()))
                    .date(DateUtil.parseLocalDate(dto.getDate()))
                    .reason(dto.getReason())
                    .startTime(DateUtil.parseLocalTime(dto.getStartTime()))
                    .endTime(DateUtil.parseLocalTime(dto.getEndTime()))
                    .employeeId(employeeBasicDetails)
                    .build());
            return messageService.messageResponse("permission.update.successfully");
        }
        throw new CustomValidationsException(messageService.messageResponse("permission.cant.modify"));
    }

    public List<PermissionDto> getAllPermission(Principal principal) {
        EmployeeBasicDetails employee = personalDetailsService.getEmployeeDetailsByPrinciple(principal);
        List<Permission> permission = permissionRepository.findAllPermission(employee.getId()).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("permission.list.empty")));
        List<PermissionDto> permissionList = new ArrayList<>();
        permission.forEach(p -> {
            String start = DateUtil.parseStringTme(p.getStartTime());
            String end = DateUtil.parseStringTme(p.getEndTime());
            String date = DateUtil.parseString(p.getDate());
            permissionList.add(PermissionDto.builder()
                    .id(String.valueOf(p.getId()))
                    .date(date)
                    .endTime(end)
                    .startTime(start)
                    .employeeId(String.valueOf(employee.getId()))
                    .build());
        });


        return permissionList;
    }

    public List<PermissionListDto> permissionFilter(PermissionFilterDto dto) {
        permissionValidation.filterValidation(dto);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PermissionListDto> cq = cb.createQuery(PermissionListDto.class);
        Root<Permission> rootPermission = cq.from(Permission.class);
        Join<Permission, EmployeeBasicDetails> employeeJoin = rootPermission.join("employeeId", JoinType.INNER);
        List<Predicate> predicateList = new ArrayList<>();
        if (dto.getDate() != null && dto.getDate().isBlank()) {
            predicateList.add(cb.equal(rootPermission.get("date"), dto.getDate()));
        }
        if (dto.getStatus() != null && dto.getStatus().isBlank()) {
            predicateList.add(cb.equal(rootPermission.get("status"), LeaveStatus.valueOf(dto.getStatus())));
        }
        cq.select(cb.construct(PermissionListDto.class,
                        rootPermission.get("id"),
                        employeeJoin.get("employeecode"),
                        cb.concat(
                                cb.concat(
                                        employeeJoin.get("firstName"),
                                        cb.concat(" ", cb.coalesce(employeeJoin.get("middleName"), ""))
                                ), cb.concat(" ", employeeJoin.get("lastName"))),
                        rootPermission.get("startTime"),
                        rootPermission.get("endTime"),
                        rootPermission.get("date")))
                .where(cb.and(predicateList.toArray(new Predicate[0])));
        TypedQuery<PermissionListDto> result = entityManager.createQuery(cq);
        return result.getResultList();
    }

    public String approvePermission(LeavePermissionDto dto) {
        permissionValidation.approvalValidation(dto);
        Permission permission = permissionRepository.findById(UUID.fromString(dto.getLeaveId())).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("permission.id.not.found")));
        permission.setStatus(LeaveStatus.valueOf(dto.getLeaveStatus()));
        permissionRepository.save(permission);
        return messageService.messageResponse("permission.update.successfully");

    }

    public List<PermissionListDto> getAllPendingPermission() {
        return permissionRepository.getAllPendingPermissions().orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("leave.request.no.data.found")));
    }

    public EmployeeWorkingTime checkPermissionTime(UUID empId, LocalDate today) {
        Permission permission = permissionRepository.findByTheyTakePermission(empId, today).orElse(null);
        if (permission != null) {
            Duration duration = Duration.between(permission.getStartTime(), permission.getEndTime());
            int totalHours = (int) duration.toHours();
            int totalMinutes = (int) (duration.toMinutes() % 60);
            return EmployeeWorkingTime.builder().hours(totalHours).minutes(totalMinutes).build();
        }

        return null;
    }

    public int getWeekNo(LocalDate date, DayOfWeek dayOfWeek){
       return permissionValidation.getWeekNumber(date,dayOfWeek);
    }
}
