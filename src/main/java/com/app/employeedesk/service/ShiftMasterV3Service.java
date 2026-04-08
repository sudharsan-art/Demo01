package com.app.employeedesk.service;

import com.app.employeedesk.dto.ShiftMasterV3Dto;
import com.app.employeedesk.entity.ShiftMasterV3;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.ShiftMasterV3Repository;
import com.app.employeedesk.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftMasterV3Service {

    private final ShiftMasterV3Repository shiftRepository;

    public String createShift(ShiftMasterV3Dto dto) {
        if (shiftRepository.existsByShiftCode(dto.getShiftCode())) {
            throw new CustomValidationsException("Shift code already exists");
        }

        ShiftMasterV3 shift = ShiftMasterV3.builder()
                .shiftCode(dto.getShiftCode())
                .shiftName(dto.getShiftName())
                .startTime(LocalTime.parse(dto.getStartTime()))
                .endTime(LocalTime.parse(dto.getEndTime()))
                .breakStartTime(dto.getBreakStartTime() != null ? LocalTime.parse(dto.getBreakStartTime()) : null)
                .breakEndTime(dto.getBreakEndTime() != null ? LocalTime.parse(dto.getBreakEndTime()) : null)
                .graceInTime(dto.getGraceInTime() != null ? dto.getGraceInTime() : 0)
                .graceOutTime(dto.getGraceOutTime() != null ? dto.getGraceOutTime() : 0)
                .minHoursFullDay(dto.getMinHoursFullDay() != null ? dto.getMinHoursFullDay() : 8.0)
                .minHoursHalfDay(dto.getMinHoursHalfDay() != null ? dto.getMinHoursHalfDay() : 4.0)
                .isNightShift(dto.getIsNightShift() != null ? dto.getIsNightShift() : false)
                .build();

        shiftRepository.save(shift);
        return "Shift created successfully";
    }

    public List<ShiftMasterV3Dto> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public String deleteShift(UUID id) {
        if (!shiftRepository.existsById(id)) {
            throw new CustomValidationsException("Shift not found");
        }
        shiftRepository.deleteById(id);
        return "Shift deleted successfully";
    }

    private ShiftMasterV3Dto mapToDto(ShiftMasterV3 shift) {
        return ShiftMasterV3Dto.builder()
                .id(shift.getId())
                .shiftCode(shift.getShiftCode())
                .shiftName(shift.getShiftName())
                .startTime(shift.getStartTime().toString())
                .endTime(shift.getEndTime().toString())
                .breakStartTime(shift.getBreakStartTime() != null ? shift.getBreakStartTime().toString() : null)
                .breakEndTime(shift.getBreakEndTime() != null ? shift.getBreakEndTime().toString() : null)
                .graceInTime(shift.getGraceInTime())
                .graceOutTime(shift.getGraceOutTime())
                .minHoursFullDay(shift.getMinHoursFullDay())
                .minHoursHalfDay(shift.getMinHoursHalfDay())
                .isNightShift(shift.getIsNightShift())
                .build();
    }
}
