package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeeLeaveConfigViewDto;
import com.app.employeedesk.dto.LeavePolicyDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.LeavePolicy;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.LeavePolicyRepository;
import com.app.employeedesk.repo.UserDetailsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeavePolicyService {

    private final LeavePolicyRepository leavePolicyRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final MessageService messageService;


    public EmployeeLeaveConfigViewDto getEmployeeLEaveConfig(Principal principal) {
        UserDetails user = (UserDetails) userDetailsRepository.findByUserName(principal.getName()).orElseThrow(() -> new CustomValidationsException("user not found"));
        EmployeeBasicDetails emp = employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);

        List<LeavePolicyDto> policies = getOrInitializeEmployeePolicies(user)
                .stream()
                .map(this::map)
                .toList();

        return EmployeeLeaveConfigViewDto.builder()
                .employeeId(emp.getId().toString())
                .employeeName(emp.getFirstName() + " " + emp.getLastName())
                .role(user.getRole().name())
                .allowedLeaves(policies)
                .build();
    }

    public List<LeavePolicyDto> getPoliciesByEmployee(String employeeId, Principal principal) {
        requireAdminOrHr(principal);

        UserDetails employee = userDetailsRepository.findById(UUID.fromString(employeeId))
                .orElseThrow(() -> new CustomValidationsException("employee not found"));

        return getOrInitializeEmployeePolicies(employee)
                .stream()
                .map(this::map)
                .toList();
    }

    public LeavePolicyDto createOrUpdate(LeavePolicyDto dto, Principal principal) {
        requireAdminOrHr(principal);

        if (dto.getEmployeeId() == null || dto.getEmployeeId().isBlank()) {
            throw new CustomValidationsException("employeeId is required");
        }

        UserDetails employee = userDetailsRepository.findById(UUID.fromString(dto.getEmployeeId()))
                .orElseThrow(() -> new CustomValidationsException("employee not found"));

        LeavePolicy entity = dto.getId() != null
                ? leavePolicyRepository.findById(UUID.fromString(dto.getId())).orElse(LeavePolicy.builder().id(UUID.randomUUID()).build())
                : leavePolicyRepository.findByEmployeeIdAndLeaveCode(employee.getId(), dto.getLeaveCode())
                .orElse(LeavePolicy.builder().id(UUID.randomUUID()).build());

        entity.setRole(employee.getRole().name());
        entity.setEmployee(employee);
        entity.setLeaveCode(dto.getLeaveCode());
        entity.setLeaveName(dto.getLeaveName());
        entity.setDaysPerMonth(dto.getDaysPerMonth());
        entity.setDaysPerYear(dto.getDaysPerYear());
        entity.setPaid(Boolean.TRUE.equals(dto.getPaid()));
        entity.setActive(dto.getActive() == null || dto.getActive());


        return map(leavePolicyRepository.save(entity));
    }

    private List<LeavePolicy> getOrInitializeEmployeePolicies(UserDetails employee) {
        List<LeavePolicy> existing = leavePolicyRepository.findByEmployeeIdAndActiveTrue(employee.getId());
        if (!existing.isEmpty()) {
            return existing;
        }

        List<LeavePolicy> templates = leavePolicyRepository
                .findByRoleAndEmployeeIsNullAndActiveTrue(employee.getRole().name());

        if (templates.isEmpty()) {
            return List.of();
        }
        List<LeavePolicy> copied = templates.stream()
                .map(template -> LeavePolicy.builder()
                        .id(UUID.randomUUID())
                        .role(employee.getRole().name())
                        .employee(employee)
                        .leaveCode(template.getLeaveCode())
                        .leaveName(template.getLeaveName())
                        .daysPerMonth(template.getDaysPerMonth())
                        .daysPerYear(template.getDaysPerYear())
                        .paid(Boolean.TRUE.equals(template.getPaid()))
                        .active(Boolean.TRUE.equals(template.getActive()))
                        .build())
                .toList();

        return leavePolicyRepository.saveAll(copied);
    }
    private LeavePolicyDto map(LeavePolicy leavePolicy) {
        LeavePolicyDto dto = new LeavePolicyDto();
        dto.setId(leavePolicy.getId().toString());
        dto.setRole(leavePolicy.getRole());
        dto.setEmployeeId(
                leavePolicy.getEmployee() != null ? leavePolicy.getEmployee().getId().toString() : null
        );
        dto.setLeaveCode(leavePolicy.getLeaveCode());
        dto.setLeaveName(leavePolicy.getLeaveName());
        dto.setDaysPerMonth(leavePolicy.getDaysPerMonth());
        dto.setDaysPerYear(leavePolicy.getDaysPerYear());
        dto.setPaid(leavePolicy.getPaid());
        dto.setActive(leavePolicy.getActive());
        return dto;
    }

    private UserDetails getUserByUserName(String username) {
        return userDetailsRepository.findByUserName(username)
                .orElseThrow(() ->
                        new CustomValidationsException(messageService.messageResponse("user.not.found")));
    }

    private void requireAdminOrHr(Principal principal) {
        getUserByUserName(principal.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean allowed = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a ->
                        "ROLE_ADMIN".equals(a.getAuthority()) ||
                                "ROLE_HR_ADMIN".equals(a.getAuthority()));

        if (!allowed) {
            throw new CustomValidationsException("Only ADMIN/HR can manage employee leave policy");
        }
    }

}
