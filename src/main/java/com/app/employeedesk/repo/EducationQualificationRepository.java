package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EducationQualification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationQualificationRepository extends JpaRepository<EducationQualification, UUID> {

    EducationQualification findByQualificationName(String name);

}
