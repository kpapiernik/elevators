package com.krzysztofpapiernik.interview.elevators.dto;

import java.util.List;

public record GetElevatorCallsDto(Long id, List<Integer> calls) {
}
