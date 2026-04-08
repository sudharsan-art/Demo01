package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeeShiftAssignDTO;
import com.app.employeedesk.dto.EmployeeShiftResponseDTO;
import com.app.employeedesk.entity.EmployeeShiftV2;
import com.app.employeedesk.entity.ShiftMasterV2;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.repo.EmployeeShiftV2Repository;
import com.app.employeedesk.repo.ShiftMasterV2Repository;
import com.app.employeedesk.repo.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EmployeeShiftV2Service {
    @Autowired
    private EmployeeShiftV2Repository employeeShiftRepository;

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private ShiftMasterV2Repository shiftRepository;

    // ASSIGN SHIFT

    public String assignShift(EmployeeShiftAssignDTO dto) {

        UUID empId = validateUUID(dto.getEmployeeId());

        UserDetails employee = userRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ShiftMasterV2 shift = shiftRepository.findById(dto.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        //  VALIDATION: one shift per date
        Optional<EmployeeShiftV2> existing =
                employeeShiftRepository
                        .findTopByEmployeeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                                employee, dto.getEffectiveDate()
                        );

        if (existing.isPresent() &&
                existing.get().getEffectiveDate().equals(dto.getEffectiveDate())) {
            throw new RuntimeException("Shift already assigned for this date");
        }

        EmployeeShiftV2 entity = EmployeeShiftV2.builder()
                .id(UUID.randomUUID().toString())
                .employee(employee)
                .shift(shift)
                .effectiveDate(dto.getEffectiveDate())
                .build();

        employeeShiftRepository.save(entity);

        return "Shift assigned successfully";
    }

    public EmployeeShiftResponseDTO getCurrentShift(String empId) {

        UUID employeeId = validateUUID(empId);

        UserDetails employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeShiftV2 shift = employeeShiftRepository
                .findTopByEmployeeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                        employee, LocalDate.now()
                )
                .orElseThrow(() -> new RuntimeException("No shift assigned"));

        return EmployeeShiftResponseDTO.builder()
                .employeeId(employeeId.toString())
                .employeeName(employee.getName())
                .shiftName(shift.getShift().getShiftName())
                .startTime(shift.getShift().getStartTime())
                .endTime(shift.getShift().getEndTime())
                .effectiveDate(shift.getEffectiveDate())
                .build();
    }
    private UUID validateUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new RuntimeException("Invalid UUID");
        }
    }

}
