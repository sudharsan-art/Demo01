package com.app.employeedesk.service;

import com.app.employeedesk.entity.AttendanceV2;
import com.app.employeedesk.entity.UserDetails;
import com.app.employeedesk.repo.AttendanceV2Repository;
import com.app.employeedesk.repo.HolidayV2Repository;
import com.app.employeedesk.repo.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceSchedulerService {
    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private AttendanceV2Repository attendanceRepo;

    @Autowired
    private HolidayV2Repository holidayRepository;

    @Scheduled(cron = "0 59 23 * * ?") // 11:59 PM daily
    public void markAbsent() {

        List<UserDetails> employees = userRepo.findAll();

        LocalDate today = LocalDate.now();

        for (UserDetails emp : employees) {

            boolean exists = attendanceRepo
                    .existsByEmployeeAndAttendanceDate(emp, today);

            if (!exists) {
                AttendanceV2 a = new AttendanceV2();
                a.setId(UUID.randomUUID().toString());
                a.setEmployee(emp);
                a.setAttendanceDate(today);
                a.setStatus("ABSENT");

                attendanceRepo.save(a);
            }
            if (holidayRepository.existsByDate(today)) {
                continue; // skip marking absent
            }
        }
    }
}
