package com.krzysztofpapiernik.interview.elevators.repository;

import com.krzysztofpapiernik.interview.elevators.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElevatorRepository extends JpaRepository<Elevator, Long> {
    Optional<Elevator> findElevatorById(Long id);
}
