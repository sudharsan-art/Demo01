package com.app.employeedesk.repo;

import com.app.employeedesk.dto.HolidayDto;
import com.app.employeedesk.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HolidayRepository extends JpaRepository<Holiday, UUID> {
    @Query(value = "select new com.app.employeedesk.dto.HolidayDto(p.id,p.date,p.reason) from Holiday p where YEAR (p.date)=:year")
    List<HolidayDto> findALLThisYearHoliday(int year);

    @Query(value = "select h.date from Holiday h where year (h.date)=:thisYear")
    List<LocalDate> findAllHolidayDateForThisYear(int thisYear);

    Optional<Holiday> findByDate(LocalDate date);

    @Query(value = "select case  when count(*) >=1 then true else false end " +
            "from Holiday h where h.date=:givenDate and h.id!=:id")
    boolean isValidHoliday(UUID id, LocalDate givenDate);

    @Query("select h from Holiday h where h.date=:date")
    Holiday checkHolidayOrNot(LocalDate date);
}
