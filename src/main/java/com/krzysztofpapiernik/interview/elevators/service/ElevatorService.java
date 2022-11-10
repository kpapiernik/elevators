package com.krzysztofpapiernik.interview.elevators.service;

import com.krzysztofpapiernik.interview.elevators.dto.CreateCallDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorCallsDto;
import com.krzysztofpapiernik.interview.elevators.dto.GetElevatorDto;
import com.krzysztofpapiernik.interview.elevators.exception.ElevatorServiceException;
import com.krzysztofpapiernik.interview.elevators.model.Elevator;
import com.krzysztofpapiernik.interview.elevators.repository.ElevatorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ElevatorService {

    private final ElevatorRepository elevatorRepository;

    @Value("${elevatorService.maxNumberOfElevators}")
    protected final Integer MAX_NUMBER_OF_ELEVATORS = 1;

    @Value("${elevator.highestFloor}")
    protected final Integer HIGHEST_FLOOR = 5;

    @Value("${elevator.lowestFloor}")
    protected final Integer LOWEST_FLOOR = -1;

    public ElevatorService(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }

    public GetElevatorDto addElevator(){

        var elevatorList = elevatorRepository.findAll();

        if(elevatorList.size() > MAX_NUMBER_OF_ELEVATORS){
            throw new ElevatorServiceException(Map.of("number of elevators", "has reached maximum value [%d]".formatted(MAX_NUMBER_OF_ELEVATORS)));
        }
        return elevatorRepository
                .save(Elevator.builder().build())
                .toGetElevatorDto();
    }

    public GetElevatorDto getElevator(Long id){
        return elevatorRepository
                .findElevatorById(id)
                .map(Elevator::toGetElevatorDto)
                .orElseThrow(() -> new ElevatorServiceException(Map.of("id", "Elevator with id %d does not exist".formatted(id))));
    }

    public List<GetElevatorDto> getAllElevators(){
        return elevatorRepository
                .findAll()
                .stream()
                .map(Elevator::toGetElevatorDto)
                .toList();
    }

    public void deleteElevator(Long id){
        var elevator = elevatorRepository
                .findElevatorById(id)
                .orElseThrow(() -> new ElevatorServiceException(Map.of("id", "Elevator with id %d does not exist".formatted(id))));

        elevatorRepository.delete(elevator);
    }

    public GetElevatorCallsDto makeCall(CreateCallDto dto){
        var elevator = elevatorRepository
                .findElevatorById(dto.id())
                .orElseThrow(() -> new ElevatorServiceException(Map.of("id", "Elevator with id %d does not exist".formatted(dto.id()))));

        if(dto.floor() > HIGHEST_FLOOR || dto.floor() < LOWEST_FLOOR){
            throw new ElevatorServiceException(Map.of("floor", "floor value is out of the range: %d - %d".formatted(LOWEST_FLOOR, HIGHEST_FLOOR)));
        }

        return elevatorRepository
                .save(elevator.withCallAdded(dto.floor()))
                .toGetElevatorCalls();
    }

    public List<GetElevatorDto> moveAllElevators(){
        return elevatorRepository
                .findAll()
                .stream()
                .map(elevator -> elevatorRepository.save(elevator.withChangedPosition()))
                .map(Elevator::toGetElevatorDto)
                .toList();
    }
}
