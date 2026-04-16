package com.app.employeedesk.service;

import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.OdRequest;
import com.app.employeedesk.repo.EmployeeBasicDetailsRepository;
import com.app.employeedesk.repo.OdRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OdRequestService {

    private final OdRequestRepository odRequestRepository;
    private final EmployeeBasicDetailsRepository employeeRepository;

    @Transactional
    public String applyOdRequest(String employeeId, LocalDate odDate, String reason, String location) {
        
        EmployeeBasicDetails employee = employeeRepository.findById(UUID.fromString(employeeId))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Check for existing OD request for the same date that isn't rejected
        Optional<OdRequest> existing = odRequestRepository.findByEmployeeIdAndOdDateAndStatus(
                employeeId, odDate, "PENDING");
        
        if (existing.isPresent()) {
            throw new RuntimeException("An OD request is already pending for this date");
        }

        OdRequest request = new OdRequest();
        request.setEmployee(employee);
        request.setOdDate(odDate);
        request.setReason(reason);
        request.setLocation(location);
        request.setStatus("PENDING");
        request.setAppliedOn(LocalDate.now());

        odRequestRepository.save(request);

        return "OD Request applied successfully";
    }

    public List<OdRequest> getEmployeeOdRequests(String employeeId) {
        return odRequestRepository.findByEmployeeId(employeeId);
    }
}
