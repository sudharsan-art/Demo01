package com.app.employeedesk.dto;

import com.app.employeedesk.entity.EmployeeBasicDetails;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

import java.net.http.HttpHeaders;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDocumentsDto {
    private String documentName;
    private byte[] files;
    private UUID employeeId;
    private HttpHeaders httpHeaders;

}
