package com.krzysztofpapiernik.interview.elevators.dto;

import com.krzysztofpapiernik.interview.elevators.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateCallDtoTest {

    @Test
    void itShouldCreateValidDto() {
        //Given
        Long id = 1L;
        Integer floor = 10;
        //When
        var result = new CreateCallDto(id, floor);
        //Then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.floor()).isEqualTo(floor);
    }

    @Test
    void itShouldNotCreateDtoWhenIdIsNull() {
        //Given
        Long id = null;
        Integer floor = 10;
        //When
        //Then
        assertThatThrownBy(() -> {var result = new CreateCallDto(id, floor);})
                .isInstanceOf(ValidationException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("id", "is null"));
    }

    @Test
    void itShouldNotCreateDtoWhenFloorIsNull() {
        //Given
        Long id = 1L;
        Integer floor = null;
        //When
        //Then
        assertThatThrownBy(() -> {var result = new CreateCallDto(id, floor);})
                .isInstanceOf(ValidationException.class)
                .hasFieldOrPropertyWithValue("errors", Map.of("floor", "is null"));
    }
}