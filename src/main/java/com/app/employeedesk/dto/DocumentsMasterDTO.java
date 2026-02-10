package com.app.employeedesk.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentsMasterDTO {

    private UUID id;
    private String documentsName;
    private String documentStatus;

}
