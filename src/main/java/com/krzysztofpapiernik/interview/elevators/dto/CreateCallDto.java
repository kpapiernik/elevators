package com.krzysztofpapiernik.interview.elevators.dto;

import com.krzysztofpapiernik.interview.elevators.exception.ValidationException;

import java.util.HashMap;

public record CreateCallDto(Long id, Integer floor) {

    public CreateCallDto{
        var errors = new HashMap<String, String>();

        if(id == null){
            errors.put("id", "is null");
        }

        if(floor == null){
            errors.put("floor", "is null");
        }

        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
