package com.app.employeedesk.service;

import com.app.employeedesk.dto.CityDto;
import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.entity.City;
import com.app.employeedesk.entity.State;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CityRepository;
import com.app.employeedesk.repo.CountryRepository;
import com.app.employeedesk.repo.StateRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.validation.MasterValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StateService {
    public final StateRepository stateRepository;
    public final CityRepository cityRepository;
    public final MasterValidation masterValidation;
    public final CountryRepository countryRepository;
    private final MessageService messageService;
    private final CityService cityService;

    public State dtoToEntity(StateDto stateDto){
        return State.builder().id(stateDto.getId())
                .asName(stateDto.getAsName())
                .name(stateDto.getName())
                .country(countryRepository.findByName(stateDto.getCountryName())).build();


    }
    public StateDto entityToDto(State state){
        return StateDto.builder()
                .id(state.getId())
                .name(state.getName())
                .asName(state.getAsName())
                .countryName(state.getCountry().getName()).build();
    }
    public List<StateDto> viewall(){
        List<State> states =stateRepository.findAll();
        List<StateDto> stateDtoList = new ArrayList<>();
        for(State i: states){
            stateDtoList.add(entityToDto(i));
        }
        return stateDtoList;

    }
    public StateDto viewByName(String stateName) throws CustomValidationsException {
        State state=stateRepository.findByName(stateName);
        if(state==null){
            throw new CustomValidationsException(messageService.messageResponse("state.not.available"));
        }
        return entityToDto(state);
    }
    public String create(StateDto stateDto) throws CustomValidationsException {
        List<String> errorMessage=new ArrayList<>();
        masterValidation.stateValidation(stateDto,errorMessage);
        if(stateRepository.findAll().stream().anyMatch(o->o.getName().equals(stateDto.getName()))){
            errorMessage.add(messageService.messageResponse("state.duplicate"));
        }
        if(stateRepository.findAll().stream().anyMatch(o->o.getAsName().equals(stateDto.getAsName()))){
            errorMessage.add(messageService.messageResponse("state.duplicate"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        stateRepository.save(dtoToEntity(stateDto));
        return messageService.messageResponse("state.create");

    }
    public StateDto getState(UUID id) throws CustomValidationsException {
        Optional<State> state=stateRepository.findById(id);
        if(state.isEmpty()) {
            throw new CustomValidationsException(messageService.messageResponse("state.not.present"));
        }
        return entityToDto(state.get());
    }
    public String update(StateDto stateDto) throws CustomValidationsException {
        List<String> errorMessage=new ArrayList<>();
        masterValidation.stateValidation(stateDto,errorMessage);
        List<State> stateList=stateRepository.findAll();
        if(stateList.stream().filter(e->!e.getId().equals(stateDto.getId())).filter(o->o.getName().equals(stateDto.getName())).toList().size()>1){
            errorMessage.add(messageService.messageResponse("state.update.duplicate"));
        }
        if(stateList.stream().filter(e->!e.getId().equals(stateDto.getId())).filter(o->o.getAsName().equals(stateDto.getAsName())).toList().size()>1){
            errorMessage.add(messageService.messageResponse("state.update.duplicate"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        stateRepository.save(dtoToEntity(stateDto));
        return messageService.messageResponse("state.update");
    }

    public String delete(UUID id) throws CustomValidationsException {
        Optional<State> city=stateRepository.findById(id);
        if(city.isPresent() && !city.get().getCityList().isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("state.parent.city.not.delete"));
        }
        stateRepository.deleteById(id);
        return messageService.messageResponse("state.delete");

    }
    public List<CityDto> findCityByState(StateDto stateDto){
       List<City> cityList= stateRepository.findCityByState(stateDto.getId());
       List<CityDto>  cityDtoList=new ArrayList<>();
       for(City i:cityList){
           cityDtoList.add(cityService.entityToDto(i));
       }
       return cityDtoList;
    }



}
