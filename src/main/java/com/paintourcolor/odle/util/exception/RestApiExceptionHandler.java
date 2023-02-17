package com.paintourcolor.odle.util.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity UsernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("404");
        restApiException.setHttpStatus(HttpStatus.NOT_FOUND);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity DisabledExceptionHandler(DisabledException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("403");
        restApiException.setHttpStatus(HttpStatus.FORBIDDEN);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.FORBIDDEN);
    }

}
