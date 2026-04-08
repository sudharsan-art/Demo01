package com.app.employeedesk.repo;

import com.app.employeedesk.entity.ShiftMasterV3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftMasterV3Repository extends JpaRepository<ShiftMasterV3, UUID> {
    boolean existsByShiftCode(String shiftCode);
}
