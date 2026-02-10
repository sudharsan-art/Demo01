package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeeDocumentsDto;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeDocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeDocumentsRepository extends JpaRepository<EmployeeDocumentsEntity, UUID> {
    List<EmployeeDocumentsEntity> findByEmployeeBasicDetails(EmployeeBasicDetails employeeBasicDetails);
    @Query("select  count(d)>0  from EmployeeDocumentsEntity d where d.documentName=:documentName and d.employeeBasicDetails=:employeeObject")
    boolean documentDuplicate(String documentName,EmployeeBasicDetails employeeObject);

    @Query("select new com.app.employeedesk.dto.EmployeeDocumentsDto(d.documentName,d.files,d.employeeBasicDetails.id) from EmployeeDocumentsEntity d where d.employeeBasicDetails=:employeeObject")
    List<EmployeeDocumentsDto> getSavedDocumentsOfUser(EmployeeBasicDetails employeeObject);
}
