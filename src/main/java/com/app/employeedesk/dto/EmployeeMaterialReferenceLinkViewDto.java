package com.app.employeedesk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class EmployeeMaterialReferenceLinkViewDto {
    private List<CourseMaterialsOutputDto> materialsOutputDto;
    private List<EmployeeSubTopicReferenceLinkDto> employeeSubTopicReferenceLinkDto;
}
