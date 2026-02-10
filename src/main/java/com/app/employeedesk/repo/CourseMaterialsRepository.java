package com.app.employeedesk.repo;

import com.app.employeedesk.dto.CourseMaterialsOutputDto;
import com.app.employeedesk.entity.CourseMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseMaterialsRepository extends JpaRepository<CourseMaterials, UUID> {
    @Query("SELECT new com.app.employeedesk.dto.CourseMaterialsOutputDto(c.id,c.name,c.type,c.description,c.materialData) " +
            "from CourseMaterials c WHERE c.courseSubTopicDetails.id =:subTopicId")
    Optional<List<CourseMaterialsOutputDto>> findAllMaterialDetails(UUID subTopicId);
}
