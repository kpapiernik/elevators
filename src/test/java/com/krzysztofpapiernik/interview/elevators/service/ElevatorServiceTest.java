package com.krzysztofpapiernik.interview.elevators.service;

import com.krzysztofpapiernik.interview.elevators.dto.CreateCallDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorCallsDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorDto;
import com.krzysztofpapiernik.interview.elevators.exception.ElevatorServiceException;
import com.krzysztofpapiernik.interview.elevators.model.Direction;
import com.krzysztofpapiernik.interview.elevators.model.Elevator;
import com.krzysztofpapiernik.interview.elevators.repository.ElevatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ElevatorServiceTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @InjectMocks
    private ElevatorService elevatorService;


    @Test
    void itShouldSaveElevator() {
        //Given
        var elevator = Elevator.builder().build();
        given(elevatorRepository.findAll()).willReturn(List.of());
        when(elevatorRepository.save(ArgumentMatchers.any(Elevator.class))).thenReturn(elevator);
        //When
        var result = elevatorService.addElevator();
        //Then
        verify(elevatorRepository).save(ArgumentMatchers.any(Elevator.class));
    }

    @Test
    void itShouldNotSaveElevatorWhenReachLimit() {
        //Given
        given(elevatorRepository.findAll()).willReturn(List.of(
                Elevator.builder().id(1L).build(),
                Elevator.builder().id(2L).build()
        ));

        var elevatorLimit = elevatorService.MAX_NUMBER_OF_ELEVATORS;
        //When
        //Then
        assertThatThrownBy(() -> {var result = elevatorService.addElevator();})
                .isInstanceOf(ElevatorServiceException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("number of elevators", "has reached maximum value [%d]".formatted(elevatorLimit)));
    }

    @Test
    void itShouldReturnAllElevators() {
        //Given
        when(elevatorRepository.findAll()).thenReturn(List.of(
                Elevator.builder().id(1L).build(),
                Elevator.builder().id(2L).build(),
                Elevator.builder().id(3L).build()
        ));
        //When
        var result = elevatorService.getAllElevators();
        //Then
        assertThat(result)
                .hasSize(3)
                .extracting(GetElevatorDto::id)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void itShouldReturnSpecificElevator() {
        //Given
        when(elevatorRepository.findElevatorById(1L))
                .thenReturn(Optional.of(Elevator.builder().id(1L).build()));
        //When
        var result = elevatorService.getElevator(1L);
        //Then
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void itShouldNotReturnElevatorWhichNotExist() {
        //Given
        when(elevatorRepository.findElevatorById(1L))
                .thenReturn(Optional.empty());
        //When

        //Then
        assertThatThrownBy(() -> {var result = elevatorService.getElevator(1L);})
                .isInstanceOf(ElevatorServiceException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("id", "Elevator with id %d does not exist".formatted(1L)));
    }

    @Test
    void itShouldDeleteElevator() {
        //Given
        var elevator = Elevator.builder().id(1L).build();
        when(elevatorRepository.findElevatorById(1L)).thenReturn(Optional.of(elevator));
        //When
        elevatorService.deleteElevator(1L);
        //Then
        verify(elevatorRepository).delete(ArgumentMatchers.any(Elevator.class));
    }

    @Test
    void itShouldNotDeleteElevatorIfDoesNotExist() {
        //Given
        when(elevatorRepository.findElevatorById(1L))
                .thenReturn(Optional.empty());
        //When

        //Then
        assertThatThrownBy(() -> {elevatorService.deleteElevator(1L);})
                .isInstanceOf(ElevatorServiceException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("id", "Elevator with id %d does not exist".formatted(1L)));
    }

    @Test
    void itShouldAddCallToElevator() {
        //Given
        var dto = new CreateCallDto(1L, 3);
        var elevator = Elevator.builder().id(1L).build();
        when(elevatorRepository.findElevatorById(1L)).thenReturn(Optional.of(elevator));
        when(elevatorRepository.save(ArgumentMatchers.any(Elevator.class))).thenReturn(elevator);
        //When
        var result = elevatorService.makeCall(dto);
        //Then
        assertThat(result)
                .extracting(GetElevatorCallsDto::calls)
                .isEqualTo(List.of(3));
    }

    @ParameterizedTest
    @MethodSource("testValuesForFloors")
    void itShouldNotAddCallToElevatorIfFloorIsOutOfRange(Integer testValue) {
        //Given
        var dto = new CreateCallDto(1L, testValue);
        var elevator = Elevator.builder().id(1L).build();
        when(elevatorRepository.findElevatorById(1L)).thenReturn(Optional.of(elevator));
        //When
        //Then
        assertThatThrownBy(() -> {elevatorService.makeCall(dto);})
                .isInstanceOf(ElevatorServiceException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("floor", "floor value is out of the range: %d - %d".formatted(elevatorService.LOWEST_FLOOR, elevatorService.HIGHEST_FLOOR)));
    }

    static Stream<Integer> testValuesForFloors(){
        return Stream.of(-2, 6);
    }

    @Test
    void itShouldNotAddCallIfElevatorDoesNotExist() {
        //Given
        when(elevatorRepository.findElevatorById(1L))
                .thenReturn(Optional.empty());

        var dto = new CreateCallDto(1L, 3);
        //When
        //Then
        assertThatThrownBy(() -> {elevatorService.makeCall(dto);})
                .isInstanceOf(ElevatorServiceException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("id", "Elevator with id %d does not exist".formatted(dto.id())));
    }

    @Test
    void itShouldMoveElevator() {
        //Given
        var elevator = Elevator.builder().id(1L).currentFloor(1).targetFloor(2).direction(Direction.UP).calls(new HashSet<>(Set.of(4, 5))).build();
        given(elevatorRepository.findAll()).willReturn(List.of(elevator));
        when(elevatorRepository.save(ArgumentMatchers.any(Elevator.class))).thenReturn(elevator.withChangedPosition());
        //When
        var result = elevatorService.moveAllElevators();
        //Then
        assertThat(result)
                .extracting(GetElevatorDto::currentFloor)
                .isEqualTo(List.of(2));

        assertThat(result)
                .extracting(GetElevatorDto::targetFloor)
                .isEqualTo(List.of(4));
    }
}