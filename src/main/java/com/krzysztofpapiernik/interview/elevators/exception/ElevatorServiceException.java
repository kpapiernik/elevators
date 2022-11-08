package com.krzysztofpapiernik.interview.elevators.exception;

import java.util.Map;

public class ElevatorServiceException extends RuntimeException{

    private Map<String, String> errors;

    public ElevatorServiceException(Map<String, String> errors){
        this.errors = errors;
    }

    public ElevatorServiceException(String message){
        super(message);
    }

    public Map<String, String> getErrors(){
        return this.errors;
    }
}
