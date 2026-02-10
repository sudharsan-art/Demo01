package com.app.employeedesk.service;

import com.app.employeedesk.dto.CountryDto;
import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.entity.Country;

import com.app.employeedesk.entity.State;
import com.app.employeedesk.exception.ListOfValidationException;

import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CountryRepository;
import com.app.employeedesk.repo.StateRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.MasterValidation;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final MasterValidation masterValidation;
    private final MessageService messageService;
    private final StateService stateService;

    public Country dtoToEntity(CountryDto countryObject){
        return Country.builder()
                .id(countryObject.getId())
                .name(countryObject.getName())
                .asName(countryObject.getAsName())
                .phoneCode(countryObject.getPhoneCode())
                .build();

    }
    public CountryDto entityToDto(Country country){
        return CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .asName(country.getAsName())
                .phoneCode(country.getPhoneCode())
                .build();
    }

    public String  countryCreate(CountryDto countryObject) throws CustomValidationsException {

        List<String> errorMessage=new ArrayList<>();
        masterValidation.countryValidation(countryObject,errorMessage);
        List<Country> countryList=countryRepository.findAll();
        if(countryList.stream().anyMatch(e->e.getName().equals(countryObject.getName()))){
            errorMessage.add(messageService.messageResponse("country.duplicates"));

        }
        if(countryList.stream().anyMatch(e->e.getAsName().equals(countryObject.getAsName()))){
            errorMessage.add(messageService.messageResponse("countryAsName.duplicates"));

        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        countryRepository.save(dtoToEntity(countryObject));


        return messageService.messageResponse("country.create");
    }

    public List<CountryDto> viewAll() throws CustomValidationsException {


      return countryRepository.viewall().orElseThrow(()->new CustomValidationsException(messageService.messageResponse("CountryPresent.no")));

    }
    public CountryDto viewByName(String countryName) throws CustomValidationsException {

        Optional<Country> country= Optional.ofNullable(countryRepository.findByName(countryName));
        if(country.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("CountryPresent.id.no"));
        }
        return entityToDto(country.get());
    }
    public Object getCountry(UUID id) throws CustomValidationsException {
        Optional<Country> country=countryRepository.findById(id);
        if(country.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("No.CountryPresent"));
        }
        return entityToDto(country.get());

    }
    public String updateCountry(CountryDto countryDto) throws CustomValidationsException {
        List<String> errormessage=new ArrayList<>();
        masterValidation.countryValidation(countryDto,errormessage);
        List<Country> countryList = countryRepository.findAll();

        if (countryList.stream().filter(e -> !e.getId().equals(countryDto.getId())).filter(e -> e.getName().equals(countryDto.getName())).count() > 0) {
            errormessage.add(messageService.messageResponse("country.duplicates"));
        }
        if (countryList.stream().filter(e -> !e.getId().equals(countryDto.getId())).filter(e -> e.getAsName().equals(countryDto.getAsName())).count() > 0) {
            errormessage.add(messageService.messageResponse("countryAsName.duplicates"));

        }
        if(!errormessage.isEmpty()){
            throw new ListOfValidationException(errormessage);
        }

            Country country = dtoToEntity(countryDto);
            countryRepository.save(country);
            return messageService.messageResponse("country.update");

    }
    public String deleteCountry(UUID id) throws CustomValidationsException {
        Optional<Country> country=countryRepository.findById(id);
        if(country.isPresent() && (!country.get().getStateList().isEmpty())) {
                throw new CustomValidationsException(messageService.messageResponse("country.child.not.delete"));
            }
        countryRepository.deleteById(id);
        return messageService.messageResponse("country.delete");
    }
    public List<StateDto> findStateByCountry(CountryDto countryDto){
        List<State> states= countryRepository.viewstateBycountry(countryDto.getId());
        List<StateDto> stateDtoList=new ArrayList<>();
        for(State i:states){
            stateDtoList.add(stateService.entityToDto(i));
        }
        return stateDtoList;
    }

}
