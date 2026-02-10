package com.app.employeedesk.entity;

import com.app.employeedesk.auditing.AuditWithBaseEntity;
import com.app.employeedesk.enumeration.FileType;
import com.app.employeedesk.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "course_materials")
public class CourseMaterials extends AuditWithBaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Column(name ="name" )
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FileType type;
    @Column(name ="description" )
    private String description;
    @Lob
    @Column(name = "material_data")
    private byte[] materialData;
    @Column(name ="status" )
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "sub_topic_id")
    private CourseSubTopicDetails courseSubTopicDetails;
}
