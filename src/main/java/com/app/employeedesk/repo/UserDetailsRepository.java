package com.app.employeedesk.repo;

import com.app.employeedesk.dto.EmployeeBasicDetailsViewDto;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {


    Optional<UserDetails> findByUserName(String username);


    @Query("select u from UserDetails u where u.userName=:name")
    UserDetails findByUserNamevalue(String name);

    @Query("select new com.app.employeedesk.dto.EmployeeBasicDetailsViewDto(u.employeeBasicDetails.id,u.employeeBasicDetails.employeecode" +
            ",u.employeeBasicDetails.firstName,u.employeeBasicDetails.department,u.employeeBasicDetails.desigination) " +
            "from UserDetails u where u.name=:userName")
    Optional<EmployeeBasicDetailsViewDto> findUserDetailsByUserName(String userName);

    @Query("select case when count(a)>0 then true else false end from UserDetails  a where a.role = 'ADMIN' and a.userName=:name")
    boolean adminDuplicate(String name);

    boolean existsByUserName(String email);




}
