package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CityDto;
import com.app.employeedesk.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    City findByName(String cityName);
}
