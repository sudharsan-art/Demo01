package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeWeekOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WeekOffRepository extends JpaRepository<EmployeeWeekOff, UUID> {

}
