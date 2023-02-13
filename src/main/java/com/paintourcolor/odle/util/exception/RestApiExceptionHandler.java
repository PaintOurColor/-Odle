package com.paintourcolor.odle.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("400");
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.BAD_REQUEST);

    }
}
