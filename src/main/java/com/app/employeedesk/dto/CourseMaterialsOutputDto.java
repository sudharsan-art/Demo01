package com.app.employeedesk.dto;

import com.app.employeedesk.enumeration.FileType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseMaterialsOutputDto {
    private UUID materialId;
    private String name;
    private FileType type;
    private String description;
    private byte[] materialData;
}
