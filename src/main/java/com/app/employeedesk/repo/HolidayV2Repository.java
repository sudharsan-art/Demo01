package com.app.employeedesk.repo;

import com.app.employeedesk.entity.HolidayV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface HolidayV2Repository extends JpaRepository<HolidayV2, UUID> {
    boolean existsByDate(LocalDate date);
}
