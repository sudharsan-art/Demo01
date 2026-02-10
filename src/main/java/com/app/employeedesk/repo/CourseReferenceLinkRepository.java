package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CourseMaterialsOutputDto;
import com.app.employeedesk.dto.EmployeeSubTopicReferenceLinkDto;
import com.app.employeedesk.entity.CourseReferenceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseReferenceLinkRepository extends JpaRepository<CourseReferenceLink, UUID> {

    @Query("SELECT new com.app.employeedesk.dto.EmployeeSubTopicReferenceLinkDto(cr.referenceLink) " +
            "from CourseReferenceLink cr WHERE cr.courseSubTopicDetails.id =:subTopicId")
    Optional<List<EmployeeSubTopicReferenceLinkDto>> findAllReferenceLinkDetails(UUID subTopicId);
}
