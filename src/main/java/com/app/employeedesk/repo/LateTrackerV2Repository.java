package com.app.employeedesk.repo;

import com.app.employeedesk.entity.LateTrackerV2;
import com.app.employeedesk.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LateTrackerV2Repository extends JpaRepository<LateTrackerV2, String> {

    Optional<LateTrackerV2> findByEmployeeAndMonthAndYear(UserDetails emp, int month, int year);
}
