package com.app.employeedesk.service;

import com.app.employeedesk.dto.CityDto;
import com.app.employeedesk.entity.City;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CityRepository;
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
public class CityService {
    private final MasterValidation masterValidation;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MessageService messageService;

    public City dtoToEntity(CityDto cityDto){
        return City.builder()
                .id(cityDto.getId())
                .asName(cityDto.getAsName())
                .name(cityDto.getName())
                .state(stateRepository.findByName(cityDto.getStateName()))
                .build();

    }
    public CityDto entityToDto(City city){
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .asName(city.getAsName())
                .stateName(city.getState().getName()).build();
    }

    public List<CityDto> viewAll() throws CustomValidationsException {
        List<City> cityList=cityRepository.findAll();
        List<CityDto> cityDtoList=new ArrayList<>();
        if (cityList.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("city.notPresent"));
        }
        for(City i:cityList){
            cityDtoList.add(entityToDto(i));
        }
        return cityDtoList;

    }

    public CityDto viewByName(String cityName) throws CustomValidationsException {
        City city=cityRepository.findByName(cityName);
        if(city==null){
            throw new CustomValidationsException(messageService.messageResponse("city.notPresent"));
        }
        return entityToDto(city);

    }



    public String create(CityDto cityDto) throws CustomValidationsException {
        List<String> errorMessage=new ArrayList<>();
        masterValidation.cityValidation(cityDto,errorMessage);
        List<City> cityList=cityRepository.findAll();
        if(cityList.stream().anyMatch(e -> e.getName().equals(cityDto.getName()))){
            errorMessage.add(messageService.messageResponse("city.Name.duplicate"));
        }
        if(cityList.stream().anyMatch(e -> e.getAsName().equals(cityDto.getAsName()))){
            errorMessage.add(messageService.messageResponse("city.Name.duplicate"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        cityRepository.save(dtoToEntity(cityDto));
        return messageService.messageResponse("city.save.success");
    }
    public CityDto getCity(UUID id) throws CustomValidationsException {
        Optional<City> city=cityRepository.findById(id);
       if( city.isEmpty()){
            throw new CustomValidationsException(messageService.messageResponse("city.notPresent"));
        }
        return entityToDto(city.get());

    }
    public String  updateCity(CityDto cityDto) throws CustomValidationsException {
        List<String> errorMessage=new ArrayList<>();
        masterValidation.cityValidation(cityDto,errorMessage);
        List<City> cityList=cityRepository.findAll();
        if(cityList.stream().filter(e -> !e.getId().equals(cityDto.getId())).anyMatch(e -> e.getName().equals(cityDto.getName()))){
            errorMessage.add(messageService.messageResponse("city.Name.duplicate.update"));
        }
        if (cityList.stream().filter(e -> !e.getId().equals(cityDto.getId())).anyMatch(e -> e.getAsName().equals(cityDto.getAsName()))){
            errorMessage.add(messageService.messageResponse("city.Name.duplicate.update"));
        }
        if(!errorMessage.isEmpty()){
            throw new ListOfValidationException(errorMessage);
        }
        cityRepository.save(dtoToEntity(cityDto));
        return messageService.messageResponse("city.Update.Success");

    }

    public String deleteCity(UUID id) {
        cityRepository.deleteById(id);
        return messageService.messageResponse("city.delete.success");
    }




}
