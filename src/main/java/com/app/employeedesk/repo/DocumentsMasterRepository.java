package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeeDocumentsDto;
import com.app.employeedesk.entity.DocumentsMaster;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DocumentsMasterRepository extends JpaRepository<DocumentsMaster,UUID> {
    @Query("select d.documentsName from DocumentsMaster d where d.documentStatus='y'")
    List<String> documentsNecessary();





}
