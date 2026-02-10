package com.app.employeedesk.Service;

import com.app.employeedesk.dto.CountryDto;
import com.app.employeedesk.entity.Country;
import com.app.employeedesk.entity.State;
import com.app.employeedesk.exception.CustomValidationsException;
import com.app.employeedesk.repo.CountryRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.service.CountryService;
import com.app.employeedesk.validation.MasterValidation;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceCountryTest {

    @Mock
    CountryRepository countryRepository;

    @Mock
    MasterValidation masterValidation;

    @Mock
    MessageService messageService;

    @InjectMocks
    CountryService countryService;


   @Test
    public void testCountryDto() {
        List<Country> countryList=new ArrayList<>();
        doNothing().when(masterValidation).countryValidation(any(CountryDto.class), any(List.class));
        when(countryRepository.findAll()).thenReturn(countryList);
        when(messageService.messageResponse(any())).thenReturn("Country.create");
        CountryDto countryDto=new CountryDto();
        String result = countryService.countryCreate(countryDto);
        verify(countryRepository).save(any());
        Assertions.assertEquals(result, "Country.create");
    }

    @Test
    public void viewByNameFailTest() {
       Optional<Country> country1 = Optional.of(new Country());
        when(countryRepository.findByName(any())).thenReturn(null);
        when((messageService.messageResponse("CountryPresent.id.no"))).thenReturn("Country not found");
        try {
            countryService.viewByName(any());
            fail("Expected CustomValidationsException to be thrown");
        } catch (CustomValidationsException e) {
            // Assert exception message if needed
            Assertions.assertEquals("Country not found", e.getMessage());
        }


    }
    @Test
    public void viewByNamePassTest(){
       Country country=new Country(UUID.randomUUID(),"India","91","IND",new ArrayList<>());
       CountryDto countryDTO=new CountryDto();
       when(countryRepository.findByName(any())).thenReturn(country);
       countryDTO=countryService.viewByName(any());
       Assertions.assertEquals("IND",countryDTO.getAsName());
       Assertions.assertEquals("India",countryDTO.getName());

   }
   @Test
    public void updateCountry(){
       List<Country> countryList=new ArrayList<>();
       CountryDto countryDto=new CountryDto(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),"India","91","IND",null);
       Country country=new Country(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),"India","91","IND",null);

       countryList.add(new Country(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),"India","+91","IND",null));
       countryList.add(new Country(UUID.randomUUID(),"Chine","11","CHN",null));
       countryList.add(new Country(UUID.randomUUID(),"Dhubai","21","DUB",null));
       countryList.add(new Country(UUID.randomUUID(),"America","51","US",null));
       countryList.add(new Country(UUID.randomUUID(),"Canada","96","CNA",null));
       List<String> errorMessage=new ArrayList<>();

       when(countryRepository.findAll()).thenReturn(countryList);

       when(messageService.messageResponse("country.update")).thenReturn("country Updated");
       String message=countryService.updateCountry(countryDto);
       verify(countryRepository,times(1)).save(country);
       verify(masterValidation,times(1)).countryValidation(countryDto,errorMessage);
       Assertions.assertEquals("country Updated",message);


   }
   @Test
    public void deleteCountryTestFail(){
       List<State> stateList=new ArrayList<>();
       stateList.add(new State(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),"Tamil Nadu","TN",new Country(),new ArrayList<>()));
       Country country=new Country(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"),"India","91","IND",stateList);
       when(countryRepository.findById(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"))).thenReturn(Optional.of(country));
      when((messageService.messageResponse("country.child.not.delete"))).thenReturn("country cannot delete because the child is not null");
       try {
          countryService.deleteCountry(UUID.fromString("22962167-983d-42e1-93bc-8b60812eb96f"));
           fail("Expected CustomValidationsException to be thrown");
       } catch (CustomValidationsException e) {
           // Assert exception message if needed
           Assertions.assertEquals("country cannot delete because the child is not null", e.getMessage());

       }






   }


}
