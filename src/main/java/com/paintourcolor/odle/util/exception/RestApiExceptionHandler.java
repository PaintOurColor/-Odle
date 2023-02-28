package com.paintourcolor.odle.util.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationError(MethodArgumentNotValidException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("400");
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity(restApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity BadCredentialsException(BadCredentialsException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("401");
        restApiException.setHttpStatus(HttpStatus.UNAUTHORIZED);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity AccessDeniedException(AccessDeniedException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("403");
        restApiException.setHttpStatus(HttpStatus.FORBIDDEN);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity EntityNotFoundException(EntityNotFoundException e) {
        RestApiException restApiException = new RestApiException();
        restApiException.setErrorCode("404");
        restApiException.setHttpStatus(HttpStatus.NOT_FOUND);
        restApiException.setErrorMessage(e.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.NOT_FOUND);
    }
}
