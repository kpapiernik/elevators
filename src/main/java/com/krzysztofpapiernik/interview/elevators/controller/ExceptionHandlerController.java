package com.krzysztofpapiernik.interview.elevators.controller;

import com.krzysztofpapiernik.interview.elevators.controller.dto.ResponseDto;
import com.krzysztofpapiernik.interview.elevators.exception.ElevatorServiceException;
import com.krzysztofpapiernik.interview.elevators.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseDto<?> validationExceptionHandler(ValidationException err){
        return new ResponseDto<>(err.getErrors());
    }

    @ExceptionHandler(ElevatorServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseDto<?> elevatorServiceExceptionHandler(ElevatorServiceException err){
        return new ResponseDto<>(err.getErrors());
    }
}
