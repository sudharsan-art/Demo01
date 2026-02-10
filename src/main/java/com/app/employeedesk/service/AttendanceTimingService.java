package com.app.employeedesk.service;

import com.app.employeedesk.dto.EmployeeWorkingTime;
import com.app.employeedesk.entity.Attendance;
import com.app.employeedesk.entity.AttendanceTiming;
import com.app.employeedesk.entity.EmployeeWeekOff;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.AttendanceTimingRepository;
import com.app.employeedesk.response.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AttendanceTimingService {
    private final AttendanceTimingRepository repository;
    private final PermissionService permissionService;
    private final MessageService messageService;

    public void saveCheckIn(Attendance attendance) {
        LocalTime time = LocalTime.now();
        repository.save(AttendanceTiming.builder()
                .inTime(time)
                .attendances(attendance).build());
    }

    public void findValidCheckOut(UUID id) {
        AttendanceTiming attendanceTiming = repository.CheckNullHereInCheckOutColumn(id).orElse(null);
        if (attendanceTiming == null) {
            throw new CustomValidationsException(messageService.messageResponse("attendance.timing.withOut.check.in.not.allow.checkout"));
        }
        attendanceTiming.setOutTime(LocalTime.now());
        repository.save(attendanceTiming);
//        boolean checkOut=true;
//
//        for (AttendanceTiming timing:attendanceTiming) {
//            if (timing.getOutTime()==null) {
//                timing.setOutTime(LocalTime.now());
//                repository.save(timing);
//                checkOut=false;
//                break;
//            }
//        }

    }
    public EmployeeWorkingTime calcuateWorkingTime(Attendance attendance){
        List<AttendanceTiming> attendanceTimings=repository.findByTotalhours(attendance.getId());
        EmployeeWorkingTime[] duration = new EmployeeWorkingTime[1];
        EmployeeWorkingTime time=new EmployeeWorkingTime();
        duration[0]=time;
        attendanceTimings.forEach(att->{
            if(att.getInTime()!=null && att.getOutTime()!=null) {
                duration[0].setHours((int) (Duration.between(att.getInTime(), att.getOutTime()).toHours() + ((duration[0]!=null)? duration[0].getHours() : 0)));

                duration[0].setMinutes((int) (Duration.between(att.getInTime(), att.getOutTime()).toMinutes() + ((duration[0]!=null)? duration[0].getMinutes() : 0)));
            }
        });
        EmployeeWorkingTime employeeWorkingTime=new EmployeeWorkingTime();
        int min=(int)duration[0].getMinutes();
        employeeWorkingTime.setHours(duration[0].getHours()+(min/60));
        employeeWorkingTime.setMinutes((min%60));
        return employeeWorkingTime;
    }





    public EmployeeWorkingTime findTotalWorkingHours(UUID id, UUID empId, LocalDate today) {
        List<AttendanceTiming> attendanceTimings = repository.findByTotalhours(id);
        EmployeeWorkingTime workingTime = permissionService.checkPermissionTime(empId, today);
        int totalHr = 0;
        int totalMin = 0;
        int[] hours = new int[attendanceTimings.size() + 1];
        int[] minute = new int[attendanceTimings.size() + 1];
        int i = 0;
        for (AttendanceTiming time : attendanceTimings) {
            if (time.getOutTime() == null) {
                break;
            }
            Duration duration = Duration.between(time.getInTime(), time.getOutTime());
            hours[i] = (int) duration.toHours();
            minute[i] = (int) (duration.toMinutes() % 60);

            i++;
        }
        if (workingTime != null) {
            hours[i] = workingTime.getHours();
            minute[i] = (int) workingTime.getMinutes();
        }
        totalHr = Arrays.stream(hours).sum();
        if (totalHr!=0) {
            int tempMin = 0;
            tempMin = Arrays.stream(minute).sum();
            totalHr += tempMin / 60;
            totalMin = tempMin % 60;
            return EmployeeWorkingTime.builder().hours(totalHr).minutes(totalMin).build();
        }
        return null;
    }

    public AttendanceTiming findByCheckInNullOrNot(UUID id) {
      return  repository.CheckNullHereInCheckOutColumn(id).orElse(null);
    }
}
