package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.DepartmentType;
import com.app.employeedesk.enumeration.DesignationType;
import com.app.employeedesk.enumeration.GenderTypes;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee_basic_info")
public class EmployeeBasicDetails extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private UUID id;
    @Column(name = "profile_img")
    private String profileImage;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderTypes gender;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "pancard_number  ")
    private String panCardNumber;
    @Column(name = "father_name")
    private String fatherName;
    @Column(name = "mother_name")
    private String motherName;
    @Column(name = "desigination")
    @Enumerated(EnumType.STRING)
    private DesignationType desigination;
    @Column(name = "department")
    @Enumerated(EnumType.STRING)
    private DepartmentType department;
    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;
    @Column(name = "emp_code")
    private String employeecode;
    @OneToOne(mappedBy = "employeeBasicDetails")
    private UserDetails userDetailsId;
    @OneToOne(mappedBy ="employeeBasicDetails" ,fetch = FetchType.LAZY)
    private EmployeeAddress employeeAddress;
    @OneToMany(mappedBy = "employeeBasicDetails")
    private List<EmployeeWorkExperience> employeeWorkExperienceList;
    @OneToMany(mappedBy = "id.employeeBasicDetails")
    private List<EducationDetails> educationDetails;
    @OneToOne(mappedBy = "employeeBasicDetails")
    private EmployeeSkillsForEmployee employeeSkillsForEmployees;
    @OneToOne(mappedBy = "employeeBasicDetails")
    private EmployeePayRollAndPaySlips payRollAndPaySlips;
    @OneToMany(mappedBy = "employeeBasicDetails")
    private List<EmployeeMonthlyPayslip> employeeMonthlyPayslips;
    @OneToMany(mappedBy = "employeeBasicDetails",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<EmployeeCourseEnrollmentDetails> employeeCourseEnrollmentDetails;
    @OneToMany(mappedBy = "employeeId",fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests;
    @OneToMany
    private List<Permission> permissions;
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private EmployeeShiftEntity employeeShiftEntity;
    @OneToMany(mappedBy = "employeeBasicDetails")
    private List<EmployeeDocumentsEntity> employeeDocumentsEntities;
}























