package com.krzysztofpapiernik.interview.elevators.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private Map<String, String> errors;
    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(message);
    }

    public Map<String, String> getErrors(){
        return errors;
    }
}
