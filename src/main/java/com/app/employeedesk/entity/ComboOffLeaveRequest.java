package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Table(name = "combo_off_leave_request")
public class ComboOffLeaveRequest extends AuditWithBaseEntity implements Serializable {
    @Column(name = "summary")
    private String workSummary;
    @Column(name = "holiday_name")
    private String holidayName;
    @Column(name ="utilized_status")
    private String utilizedStatus;
    @Column(name = "total_working_hours")
    private Integer workedHours;
    @Column(name = "dept")
    private String department;
    @JdbcTypeCode(SqlTypes.CHAR)
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private EmployeeBasicDetails employeeBasicDetails;
    @Column(name = "name")
    private String name;
    @Column(name = "combo_date")
    private LocalDate date;

}
