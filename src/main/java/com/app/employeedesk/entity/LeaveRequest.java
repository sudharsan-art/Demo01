package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.LeaveDayType;
import com.app.employeedesk.enumeration.LeaveStatus;
import com.app.employeedesk.enumeration.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Table(name = "leave_request")
public class LeaveRequest extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    @Column(name = "leave_type")
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    @Column(name = "reason")
    private String reason;
    @Column(name = "from_date")
    private LocalDate fromDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "start_date_status")
    @Enumerated(EnumType.STRING)
    private LeaveDayType startDateStatus;
    @Column(name = "end_date_status")
    @Enumerated(EnumType.STRING)
    private LeaveDayType endDateStatus;
    @Column(name = "leave_status")
    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;

    @Column(name = "leave_day_type")
    @Enumerated(EnumType.STRING)
    private LeaveDayType leaveDayType;
    @ManyToOne
    @JoinColumn (name = "employee_id")
    private EmployeeBasicDetails employeeId;

}
