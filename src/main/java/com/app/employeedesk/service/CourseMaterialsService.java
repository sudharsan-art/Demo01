package com.app.employeedesk.service;

import com.app.employeedesk.dto.CourseMaterialsDto;
import com.app.employeedesk.dto.CourseMaterialsOutputDto;
import com.app.employeedesk.dto.MessageDto;
import com.app.employeedesk.entity.CourseMaterials;
import com.app.employeedesk.entity.CourseSubTopicDetails;
import com.app.employeedesk.enumeration.FileType;
import com.app.employeedesk.enumeration.Status;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.CourseMaterialsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.file.FileUtils;
import com.app.employeedesk.validation.CourseMasterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.app.employeedesk.util.file.FileUtils.decompressFile;


@Service
@RequiredArgsConstructor
public class CourseMaterialsService {

    private final MessageService messageService;

    private final CourseMaterialsRepository courseMaterialsRepository;

    private final CourseMasterValidation courseMasterValidation;

    private final CourseSubTopicDetailsService courseSubTopicDetailsService;


    public MessageDto uploadFile(MultipartFile file, CourseMaterialsDto courseMaterialsDto) throws IOException, CustomValidationsException, ListOfValidationException {
        courseMasterValidation.materialValidation(courseMaterialsDto);
        CourseSubTopicDetails courseSubTopicDetails = courseSubTopicDetailsService.findSubtopicById(courseMaterialsDto.getSubTopicId());
        if(courseSubTopicDetails.getCourseTasksDetails().stream().anyMatch(i->i.getName().equals(courseMaterialsDto.getName()))){
            throw new CustomValidationsException(messageService.messageResponse("material.name.exist"));
        }
        courseMaterialsRepository.save(CourseMaterials.builder()
                .name(courseMaterialsDto.getName())
                .type(FileType.valueOf(courseMaterialsDto.getType()))
                .materialData(FileUtils.compressFile(file.getBytes()))
                .description(courseMaterialsDto.getDescription())
                .courseSubTopicDetails(courseSubTopicDetails)
                .status(Status.ACTIVE)
                .build());
        return MessageDto.builder().message(messageService.messageResponse("materials.details.upload")).build();
    }

    public byte[] downloadFile(UUID materialId) throws CustomValidationsException {
        Optional<CourseMaterials> materialData = courseMaterialsRepository.findById(materialId);
        return materialData.map(courseTopicMaterials -> decompressFile(courseTopicMaterials.getMaterialData()))
                .orElseThrow(() -> new CustomValidationsException(messageService.messageResponse("material.details.empty")));
    }

    public MessageDto deleteMaterial(UUID materialId) {
        courseMaterialsRepository.deleteById(materialId);
        return MessageDto.builder().message(messageService.messageResponse("materials.details.delete")).build();
    }

    public List<CourseMaterialsOutputDto> getAllMaterialDetails(UUID subTopicId) throws CustomValidationsException {
        return courseMaterialsRepository.findAllMaterialDetails(subTopicId).orElseThrow(
                () -> new CustomValidationsException(messageService.messageResponse("material.details.empty")));
    }



}
