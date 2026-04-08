package com.app.employeedesk.service;
import com.app.employeedesk.dto.LeaveMasterDto;
import com.app.employeedesk.entity.LeaveMaster;
import com.app.employeedesk.repo.LeaveMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveMasterService {

    private final LeaveMasterRepository leaveMasterRepository;

    public List<LeaveMasterDto> getAllLeaves() {

        return leaveMasterRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private LeaveMasterDto mapToDto(LeaveMaster leave) {

        return LeaveMasterDto.builder()
                .id(leave.getId())
                .leaveCode(leave.getLeaveCode())
                .leaveName(leave.getLeaveName())
                .build();
    }
}
