package com.app.employeedesk.validation;

import com.app.employeedesk.dto.CityDto;
import com.app.employeedesk.dto.CountryDto;



import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CountryRepository;
import com.app.employeedesk.repo.StateRepository;
import com.app.employeedesk.response.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.regex.Pattern;
@Component
@RequiredArgsConstructor

public class MasterValidation {

    private final MessageService messageService;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;


    public void countryValidation(CountryDto countryDto,List<String> errorMessage) throws CustomValidationsException {
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");

        if (countryDto.getName() == null || countryDto.getAsName() == null || countryDto.getName().isBlank() || countryDto.getName().isEmpty() || countryDto.getAsName().isEmpty() || countryDto.getAsName().isBlank()) {
            errorMessage.add(messageService.messageResponse("name.empty"));
        }
            if (countryDto.getName().isBlank() || countryDto.getName().isEmpty() || countryDto.getAsName().isEmpty() || countryDto.getAsName().isBlank()) {
                errorMessage.add(messageService.messageResponse("name.empty"));

            }
            if (regex.matcher(countryDto.getName()).find()) {
                errorMessage.add(messageService.messageResponse("countryName.specialCharacters"));
            }
            if (regex.matcher(countryDto.getAsName()).find()) {
                errorMessage.add(messageService.messageResponse("countryAsName.specialCharacters"));
            }
            if (countryDto.getPhoneCode().isBlank() || countryDto.getPhoneCode().isEmpty()) {
                errorMessage.add(messageService.messageResponse("phoneCode.empty"));
            }
        }


    public void stateValidation(StateDto stateDto,List<String> errorMessage) throws CustomValidationsException {
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        if(stateDto.getName()==null || stateDto.getAsName()==null||
        stateDto.getName().isBlank() ||
                stateDto.getAsName().isBlank()||
                stateDto.getName().isEmpty()||
                stateDto.getAsName().isEmpty()) {
            errorMessage.add(messageService.messageResponse("state.name.empty"));
        }
        if(regex.matcher(stateDto.getName()).find() || regex.matcher(stateDto.getAsName()).find()){
            errorMessage.add(messageService.messageResponse("state.name.specialCharacter"));
        }
        if(!countryRepository.existsByName(stateDto.getCountryName())){
            errorMessage.add(messageService.messageResponse("State.givenCountry.notAvailable"));
        }
    }

    public void cityValidation(CityDto cityDto,List<String> errorMessage) throws CustomValidationsException {
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        if (cityDto.getName() == null || cityDto.getAsName() == null || cityDto.getName().isEmpty() || cityDto.getAsName().isEmpty()
                || cityDto.getName().isBlank() || cityDto.getAsName().isBlank()) {
            errorMessage.add(messageService.messageResponse("city.name.notValid"));
        }
        if (regex.matcher(cityDto.getStateName()).find() || regex.matcher(cityDto.getName()).find()) {
            errorMessage.add(messageService.messageResponse("city.name.specialCharacter"));
        }
        if (!stateRepository.existsByName(cityDto.getStateName())) {
            errorMessage.add(messageService.messageResponse("City.state.name.notpresent"));
        }
    }







}
