package com.app.employeedesk.repo;

import com.app.employeedesk.entity.City;
import com.app.employeedesk.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StateRepository extends JpaRepository<State, UUID> {
    List<State> findAll();
    State findByName(String stateName);

    boolean existsByName(String stateName);
    @Query("select s.cityList from State s where s.id=:id")
    List<City> findCityByState(UUID id);




}
