package com.app.employeedesk.repo;

import com.app.employeedesk.entity.YearMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface YearMasterRepository extends JpaRepository<YearMaster, UUID> {

    Optional<YearMaster> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate start,
            LocalDate end
    );
}
