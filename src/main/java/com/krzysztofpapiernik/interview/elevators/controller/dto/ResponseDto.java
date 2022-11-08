package com.krzysztofpapiernik.interview.elevators.controller.dto;

import java.util.Map;

public record ResponseDto<T>(T data, Map<String, String> errors) {

    public ResponseDto(T data){
        this(data, null);
    }

    public ResponseDto(Map<String, String> errors){
        this(null, errors);
    }
}
