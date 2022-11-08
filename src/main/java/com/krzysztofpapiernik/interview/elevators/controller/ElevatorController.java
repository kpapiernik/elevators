package com.krzysztofpapiernik.interview.elevators.controller;


import com.krzysztofpapiernik.interview.elevators.controller.dto.ResponseDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorDto;
import com.krzysztofpapiernik.interview.elevators.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorService elevatorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<GetElevatorDto> addElevator(){
        return new ResponseDto<>(elevatorService.addElevator());
    }

    @GetMapping
    public ResponseDto<List<GetElevatorDto>> getAllElevators(){
        return new ResponseDto<>(elevatorService.getAllElevators());
    }

}
