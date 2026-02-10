package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CountryDto;
import com.app.employeedesk.entity.Country;
import com.app.employeedesk.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country,UUID> {
    Country findByName(String countryName);
    List<Country> findAll();
    boolean existsByName(String countryName);
    @Query("select new com.app.employeedesk.dto.CountryDto(c.id,c.name,c.phoneCode,c.asName) from Country c ")
    Optional<List<CountryDto>> viewall();
    @Query("select c.stateList from Country c where c.id=:id")
    List<State> viewstateBycountry(UUID id);

}
