package com.krzysztofpapiernik.interview.elevators.model;

import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorCallsDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "elevators")
public class Elevator {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Builder.Default
    private Integer currentFloor = 0;

    @Builder.Default
    private Integer targetFloor = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Direction direction = Direction.STOP;

    @ElementCollection
    @Builder.Default
    private Set<Integer> calls = new HashSet<>();

    public Elevator withCallAdded(Integer calledFloor){
        calls.add(calledFloor);
        return Elevator
                .builder()
                .id(id)
                .currentFloor(currentFloor)
                .targetFloor(targetFloor)
                .calls(calls)
                .build();
    }

    private Integer chooseNextFloor(){
        if(calls.isEmpty()){
            direction = Direction.STOP;
            return 0;
        }

        var sortedCalls = calls.stream().sorted().toList();

        if(direction.equals(Direction.UP)){
            return findNextFloorAbove(sortedCalls);
        }

        if(direction.equals(Direction.DOWN)){
            return findNextFloorBelow(sortedCalls);
        }

        if(direction.equals(Direction.STOP)){
            direction = Direction.UP;
            return findNextFloorAbove(sortedCalls);
        }
        return this.currentFloor;
    }

    private Integer findNextFloorAbove(List<Integer> sortedFloorCalls){
        var choseFloor = sortedFloorCalls
                .stream()
                .filter(call -> call.compareTo(currentFloor) > 0)
                .reduce(Integer::min)
                .orElseGet(() -> findNextFloorBelow(sortedFloorCalls));

        calls.remove(choseFloor);
        if(choseFloor < currentFloor){
            direction = Direction.DOWN;
        }
        return choseFloor;
    }

    private Integer findNextFloorBelow(List<Integer> sortedFloorCalls){
        var choseFloor = sortedFloorCalls
                .stream().filter(call -> call.compareTo(currentFloor) < 0)
                .reduce(Integer::max)
                .orElseGet(() -> findNextFloorAbove(sortedFloorCalls));

        calls.remove(choseFloor);
        if(choseFloor > currentFloor){
            direction = Direction.UP;
        }
        return choseFloor;
    }

    public Elevator withChangedPosition(){
        return Elevator
                .builder()
                .id(id)
                .currentFloor(targetFloor)
                .targetFloor(chooseNextFloor())
                .calls(calls)
                .build();
    }

    public GetElevatorDto toGetElevatorDto(){
        return new GetElevatorDto(id, currentFloor, targetFloor);
    }

    public GetElevatorCallsDto toGetElevatorCalls(){
        var callSetToList = calls.stream().toList();
        return new GetElevatorCallsDto(id, callSetToList);
    }
}
