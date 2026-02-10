package com.app.employeedesk.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeSkills extends JpaRepository<com.app.employeedesk.entity.EmployeeSkills, UUID> {

}
