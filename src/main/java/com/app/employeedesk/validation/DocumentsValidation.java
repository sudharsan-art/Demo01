package com.app.employeedesk.validation;

import com.app.employeedesk.dto.EmployeeDocumentsDto;
import com.app.employeedesk.dto.EmployeeDocumentsDtoList;
import com.app.employeedesk.entity.EmployeeBasicDetails;
import com.app.employeedesk.repo.DocumentsMasterRepository;
import com.app.employeedesk.repo.EmployeeDocumentsRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DocumentsValidation {
    private final MessageService messageService;
    private final BasicValidation basicValidation;
    private final DocumentsMasterRepository documentsMasterRepository;
    private final EmployeeDocumentsRepository employeeDocumentsRepository;
    public void documentValidation(MultipartFile file, List<String> errorMessage, EmployeeDocumentsDto employeeDocumentsDto, EmployeeBasicDetails employeeBasicDetails){
        if(file.getSize()>(2*1024*1024)){
            errorMessage.add(employeeDocumentsDto.getDocumentName()+messageService.messageResponse("file.large"));
        }
       String a= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if(Stream.of("pdf", "png", "jpg").noneMatch(s -> file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).equalsIgnoreCase(s))

        ){
          errorMessage.add(employeeDocumentsDto.getDocumentName()+messageService.messageResponse("file.not.standard"));

        }
        if(employeeDocumentsRepository.documentDuplicate(employeeDocumentsDto.getDocumentName(),employeeBasicDetails)){
            errorMessage.add(employeeDocumentsDto.getDocumentName()+messageService.messageResponse("document.already.present"));
        }



    }
    public void documentsNameValidation(EmployeeDocumentsDto employeeDocumentsDto,List<String> errorMessage){
        if(!basicValidation.stringValidation(employeeDocumentsDto.getDocumentName())){
                errorMessage.add(employeeDocumentsDto.getDocumentName()+messageService.messageResponse("document.invalid"));
        }

    }
    public void necessaryDocuments(EmployeeDocumentsDtoList employeeDocumentsDtoList,List<MultipartFile> files,List<String> errorMessage){
      List<String> necessaryDocuments=documentsMasterRepository.documentsNecessary();
      for(String j:necessaryDocuments) {
          boolean flag=true;
          for (EmployeeDocumentsDto i : employeeDocumentsDtoList.getEmployeeDocumentsDtos()) {
              if (i.getDocumentName().equalsIgnoreCase(j)) {
                  flag = false;
                  break;
              }
          }
          if(flag){
              errorMessage.add(j+messageService.messageResponse("document.necessary"));
          }
      }




    }


}
