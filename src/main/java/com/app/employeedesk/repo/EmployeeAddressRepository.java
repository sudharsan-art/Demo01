package com.app.employeedesk.repo;

import com.app.employeedesk.entity.EmployeeAddress;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, UUID> {

    EmployeeAddress findByEmployeeBasicDetails(EmployeeBasicDetails employeeBasicDetails);
    Boolean existsByEmployeeBasicDetails(EmployeeBasicDetails employeeBasicDetails);

}
