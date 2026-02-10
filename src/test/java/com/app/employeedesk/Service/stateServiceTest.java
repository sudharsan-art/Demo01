package com.app.employeedesk.Service;

import com.app.employeedesk.dto.StateDto;
import com.app.employeedesk.entity.Country;
import com.app.employeedesk.entity.State;
import com.app.employeedesk.exception.ListOfValidationException;
import com.app.employeedesk.repo.CountryRepository;
import com.app.employeedesk.repo.StateRepository;
import com.app.employeedesk.response.MessageService;
import com.app.employeedesk.service.StateService;
import com.app.employeedesk.validation.MasterValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class stateServiceTest {
    @InjectMocks
    StateService stateService;

    @Mock
    MasterValidation masterValidation;
    @Mock
    MessageService messageService;
    @Mock
    StateRepository stateRepository;
    @Mock
    CountryRepository countryRepository;
    public Stream<StateDto> stateDtoObject(){
        return Stream.of(
//                StateDto.builder().id(UUID.randomUUID()).name("Tamil Nadu").asName("TN").countryName("INDIA").build(),
//                StateDto.builder().id(UUID.randomUUID()).name("Kerala").asName("KL").countryName("INDIA").build(),
//                StateDto.builder().id(UUID.randomUUID()).name("Andra pradesh").asName("AP").countryName("INDIA").build(),
//                StateDto.builder().id(UUID.randomUUID()).name("Karnataka").asName("KA").countryName("INDIA").build(),
                StateDto.builder().id(UUID.randomUUID()).name("Hariayana").asName("HR").countryName("INDIA").build(),
                StateDto.builder().id(UUID.randomUUID()).name("Jammu Kashmir").asName("JK").countryName("INDIA").build()
        );
    }

   @ParameterizedTest
   @MethodSource("stateDtoObject")
    public void createStateTest(StateDto stateDto){
       List<String> errorMessage=new ArrayList<>();
       List<State> state=new ArrayList<>();
       state.add(new State(UUID.randomUUID(),"Tamil Nadu","TN",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Kerala","KL",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Andra pradesh","AP",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Karnataka","KA",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Maharastra","MH",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Madya Pradesh","MP",new Country(),new ArrayList<>()));
//       StateDto stateDto=StateDto.builder()
//                       .id(UUID.randomUUID()).name("Tamil Nadu").asName("TN").countryName("INDIA").build();

       doNothing().when(masterValidation).stateValidation(any(StateDto.class), eq(errorMessage));
       when(stateRepository.findAll()).thenReturn(state);
       when(messageService.messageResponse("state.duplicate")).thenReturn("state is already present");
       ListOfValidationException exception= Assertions.assertThrows(ListOfValidationException.class,() ->stateService.create(stateDto));
       List<String> errorString=new ArrayList<>(Arrays.asList("state is already present", "state is already present"));
       Assertions.assertEquals(errorString.stream().toList(),exception.getErrorMessages());

   }

    @ParameterizedTest
    @MethodSource("stateDtoObject")
    public void stateUpdateTest(StateDto stateDto){
       List<String> errorMessage=new ArrayList<>();
       List<State> state=new ArrayList<>();
       state.add(new State(UUID.randomUUID(),"Tamil Nadu","TN",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Kerala","KL",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Andra pradesh","AP",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Karnataka","KA",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Maharastra","MH",new Country(),new ArrayList<>()));
       state.add(new State(UUID.randomUUID(),"Madya Pradesh","MP",new Country(),new ArrayList<>()));
//       StateDto stateDto=StateDto.builder()
//                      .id(UUID.randomUUID()).name("Punjab").asName("PN").countryName("INDIA").build();
      doNothing().when(masterValidation).stateValidation(any(StateDto.class), eq(errorMessage));
       when(stateRepository.findAll()).thenReturn(state);
       when(messageService.messageResponse("state.update")).thenReturn("state update successfully");
       when(countryRepository.findByName(stateDto.getCountryName())).thenReturn(new Country());
       String message= stateService.update(stateDto);
       Assertions.assertEquals("state update successfully",message);

   }

   @Test
   public void stateDeleteTest(){
        when(stateRepository.findById(UUID.randomUUID())).thenReturn(Optional.of(new State(UUID.randomUUID(),"Tamil Nadu","TN",new Country(),new ArrayList<>())));
        when(messageService.messageResponse("state.delete")).thenReturn("state deleted Successfully");
        String message=stateService.delete(UUID.fromString("8390ddde-0332-47a0-ac80-f2e3eadfa527"));
        verify(stateRepository).deleteById(UUID.fromString("8390ddde-0332-47a0-ac80-f2e3eadfa527"));
        Assertions.assertEquals("state deleted Successfully",message);




   }


}
