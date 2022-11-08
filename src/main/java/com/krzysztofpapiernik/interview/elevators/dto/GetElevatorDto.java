package com.krzysztofpapiernik.interview.elevators.dto;

public record GetElevatorDto(Long id, Integer currentFloor, Integer targetFloor) {
}
