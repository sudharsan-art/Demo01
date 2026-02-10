package com.app.employeedesk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "education_details")
public class EducationDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private EducationDetailsEmbedable id;
    @Column(name = "university_name")
    private String institutionName;

    @Column(name = "score_type_id")
    private String scoreType;

    @Column(name = "score")
    private Double score;

    @Column(name = "from_year")
    private LocalDate fromDate;

    @Column(name = "to_year")
    private LocalDate toDate;
}
