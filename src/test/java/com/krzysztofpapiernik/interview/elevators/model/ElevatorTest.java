package com.krzysztofpapiernik.interview.elevators.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ElevatorTest {

    @Test
    void itShouldAddCall() {
        //Given
        Elevator elevator = Elevator.builder().build();
        //When
        var result = elevator.withCallAdded(10);
        //Then
        assertThat(result.calls).contains(10);
    }

    @Test
    void itShouldNotDuplicateCall() {
        //Given
        var callSet = new HashSet<>(Set.of(9, 10, 11, 12));
        Elevator elevator = Elevator.builder().calls(callSet).build();
        //When
        var result = elevator.withCallAdded(10);
        //Then
        assertThat(result.calls).hasSize(4);
    }


    @Test
    void itShouldChooseNextFloorAboveFromGroundFloor() {
        //Given
        var callSet = new HashSet<>(Set.of(2, -1, 5));
        var elevator = Elevator.builder().currentFloor(0).targetFloor(0).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(2);
    }

    @Test
    void itShouldChooseNextFloorAboveFromHigherFloor() {
        //Given
        var callSet = new HashSet<>(Set.of(2, -1, 5, 10));
        var elevator = Elevator.builder().currentFloor(8).direction(Direction.UP).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(10);
    }

    @Test
    void itShouldChangeDirectionWhenThereIsNoMoreCallsAbove() {
        //Given
        var callSet = new HashSet<>(Set.of(2, -1, 5));
        var elevator = Elevator.builder().currentFloor(8).direction(Direction.UP).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(5);
        assertThat(result.direction).isEqualTo(Direction.DOWN);
    }

    @Test
    void itShouldGoBackToGroundFloorIfThereIsNoCalls() {
        //Given
        Set<Integer> callSet = new HashSet<>();
        var elevator = Elevator.builder().currentFloor(8).direction(Direction.UP).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(0);
        assertThat(result.direction).isEqualTo(Direction.STOP);
    }

    @Test
    void itShouldChooseFloorBelowGroundFloor() {
        //Given
        var callSet = new HashSet<>(Set.of(-2, -1, -5));
        var elevator = Elevator.builder().currentFloor(5).direction(Direction.UP).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(-1);
        assertThat(result.direction).isEqualTo(Direction.DOWN);
    }

    @Test
    void itShouldChooseNextFloorBelow() {
        //Given
        var callSet = new HashSet<>(Set.of(2, -3, 5));
        var elevator = Elevator.builder().currentFloor(-1).direction(Direction.DOWN).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(-3);
    }

    @Test
    void itShouldChangeDirectionIfThereIsNoMoreCallsBelow() {
        //Given
        var callSet = new HashSet<>(Set.of(2, 10, 5));
        var elevator = Elevator.builder().currentFloor(-2).direction(Direction.DOWN).calls(callSet).build();
        //When
        var result = elevator.withChangedPosition();
        //Then
        assertThat(result.targetFloor).isEqualTo(2);
        assertThat(result.direction).isEqualTo(Direction.UP);
    }

    @Test
    void itShouldReturnGetElevatorDto() {
        //Given
        var elevatorToGet = Elevator.builder().id(1L).currentFloor(8).targetFloor(10).build();
        //When
        var result = elevatorToGet.toGetElevatorDto();
        //Then
        assertThat(result.id()).isEqualTo(elevatorToGet.id);
        assertThat(result.currentFloor()).isEqualTo(elevatorToGet.currentFloor);
        assertThat(result.targetFloor()).isEqualTo(elevatorToGet.targetFloor);
    }

    @Test
    void itShouldReturnListOfElevatorCalls() {
        //Given
        var callSet = new HashSet<>(Set.of(2, 10, 5));
        var elevator = Elevator.builder().id(1L).calls(callSet).build();
        //When
        var result = elevator.toGetElevatorCalls();
        //Then
        assertThat(result.id()).isEqualTo(elevator.id);
        assertThat(result.calls()).isEqualTo(elevator.calls.stream().toList());
    }
}