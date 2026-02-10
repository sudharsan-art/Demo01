package com.app.employeedesk.service;
import com.app.employeedesk.dto.DocumentsMasterDTO;
import com.app.employeedesk.dto.EmployeeDocumentsDto;
import com.app.employeedesk.dto.EmployeeDocumentsDtoList;
import com.app.employeedesk.entity.DocumentsMaster;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.entity.EmployeeDocumentsEntity;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.DocumentsMasterRepository;
import com.app.employeedesk.repo.EmployeeDocumentsRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.util.file.FileUtils;
import com.app.employeedesk.validation.DocumentsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmployeeDocumentsService {
    private final EmployeePersonalDetailsService employeePersonalDetailsService;
    private final DocumentsValidation documentsValidation;
    private final EmployeeDocumentsRepository employeeDocumentsRepository;
    private final DocumentsMasterRepository documentsMasterRepository;
    private final MessageService messageService;

    public DocumentsMasterDTO entityTODTO(DocumentsMaster documentsMaster){
        return DocumentsMasterDTO.builder()
                .id(documentsMaster.getId())
                .documentsName(documentsMaster.getDocumentsName())
                .documentStatus(documentsMaster.getDocumentStatus()).build();

    }

    public List<DocumentsMasterDTO> getdocumentsName(){
        return documentsMasterRepository.findAll().stream().map(this::entityTODTO).toList();
    }

    public String saveEmployeeDocuments(List<MultipartFile> files, EmployeeDocumentsDtoList employeeDocumentsDtoList, Principal principal) throws IOException {
        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        List<String> errorMessage=new ArrayList<>();
        documentsValidation.necessaryDocuments(employeeDocumentsDtoList,files,errorMessage);
        IntStream.rangeClosed(0, files.size()-1).forEach(i->documentsValidation.documentValidation(files.get(i),errorMessage,employeeDocumentsDtoList.employeeDocumentsDtos.get(i),employeeBasicDetails));
        employeeDocumentsDtoList.getEmployeeDocumentsDtos().forEach(o->documentsValidation.documentsNameValidation(o,errorMessage));
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        List<EmployeeDocumentsEntity> employeeDocumentsEntities=new ArrayList<>();
       for(int i=0;i< files.size()-1;i++){
           employeeDocumentsEntities.add(EmployeeDocumentsEntity.builder()
                   .documentName(employeeDocumentsDtoList.getEmployeeDocumentsDtos().get(i).getDocumentName())
                   .files(FileUtils.compressFile(files.get(i).getBytes()))
                   .employeeBasicDetails(employeeBasicDetails)
                   .build());
       }
       employeeDocumentsRepository.saveAll(employeeDocumentsEntities);
       return messageService.messageResponse("documents.saved.success");
    }

    public EmployeeDocumentsDtoList documentsGet(Principal principal){
//        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
//        return employeeDocumentsRepository.getSavedDocumentsOfUser(employeeBasicDetails);
        EmployeeBasicDetails employeeBasicDetails=employeePersonalDetailsService.getEmployeeDetailsByPrinciple(principal);
        List<EmployeeDocumentsEntity> employeeDocuments=employeeDocumentsRepository.findByEmployeeBasicDetails(employeeBasicDetails);
        List<byte[]> bytes1=new ArrayList<>();

        List<EmployeeDocumentsDto> employeeDocumentsDtos=new ArrayList<>();
        employeeDocuments.forEach(o-> {

            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(FileUtils.decompressFile(o.getFiles()));
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

                 bytes1.add(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            employeeDocumentsDtos.add(EmployeeDocumentsDto.builder()
                        .documentName(o.getDocumentName())
                        .files(FileUtils.decompressFile(o.getFiles()))

                        .employeeId(o.getEmployeeBasicDetails().getId()).build());

            }
            );
        return EmployeeDocumentsDtoList.builder()
                .employeeDocumentsDtos(employeeDocumentsDtos).build();



        }

    public String deleteDocuments(UUID id){
        employeeDocumentsRepository.deleteById(id);
        return messageService.messageResponse("documents.deleted.success");
    }
}
