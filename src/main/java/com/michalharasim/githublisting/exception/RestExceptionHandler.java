package com.michalharasim.githublisting.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(value = {XMLAsHeaderException.class})
    public ResponseEntity<ApiException> handleXMLHeaderException(XMLAsHeaderException e) {
        ApiException errorResponse = new ApiException(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiException> handleUserNotFoundException(UserNotFoundException e) {
        ApiException errorResponse = new ApiException(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


}
