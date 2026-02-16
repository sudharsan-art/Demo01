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

        String role = user.getRole().name();
        List<LeavePolicyDto> policies = leavePolicyRepository
                .findByRoleAndActiveTrue(role)
                .stream()
                .map(this::map)
                .toList();

        return EmployeeLeaveConfigViewDto.builder()
                .employeeId(emp.getId().toString())
                .employeeName(emp.getFirstName() + " " + emp.getLastName())
                .role(role)
                .allowedLeaves(policies)
                .build();
    }

    public List<LeavePolicyDto> getPoliciesByRole(String role) {
        return leavePolicyRepository.findByRoleAndActiveTrue(role).stream().map(this::map).toList();
    }

    private LeavePolicyDto map(LeavePolicy leavePolicy) {
        LeavePolicyDto dto = new LeavePolicyDto();
        dto.setId(leavePolicy.getId().toString());
        dto.setRole(leavePolicy.getRole());
        dto.setLeaveCode(leavePolicy.getLeaveCode());
        dto.setLeaveName(leavePolicy.getLeaveName());
        dto.setDaysPerMonth(leavePolicy.getDaysPerMonth());
        dto.setDaysPerYear(leavePolicy.getDaysPerYear());
        dto.setPaid(leavePolicy.getPaid());
        dto.setActive(leavePolicy.getActive());
        return dto;
    }

    public LeavePolicyDto createOrUpdate(LeavePolicyDto dto) {
        LeavePolicy entity = LeavePolicy.builder()
                .id(dto.getId() == null ? UUID.randomUUID() : UUID.fromString(dto.getId()))
                .role(dto.getRole())
                .leaveCode(dto.getLeaveCode())
                .leaveName(dto.getLeaveName())
                .daysPerMonth(dto.getDaysPerMonth())
                .daysPerYear(dto.getDaysPerYear())
                .paid(Boolean.TRUE.equals(dto.getPaid()))
                .active(dto.getActive() == null || dto.getActive())
                .build();
        return map(leavePolicyRepository.save(entity));
    }
}
