package com.myRetail.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ProductControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode("API_ERR_INVALID_INPUT");
            errorResponse.setErrorMessage(error.getDefaultMessage());
            errors.add(errorResponse);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorResponse>> businessExceptionHandler(BusinessException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(String.valueOf(ex.getResponseStatusCode()));
        response.setErrorMessage(ex.getErrorMessage());
        return new ResponseEntity<>(Collections.singletonList(response), ex.getHttpStatus());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<List<ErrorResponse>> httpClientErrorExceptionHandler(ApplicationException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(String.valueOf(ex.getResponseStatusCode()));
        response.setErrorMessage(ex.getErrorMessage());
        return new ResponseEntity<>(Collections.singletonList(response), ex.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<List<ErrorResponse>> errorHandler(RuntimeException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode("API_ERR_GENERIC_ERROR");
        response.setErrorMessage("Oops! An exception has occurred.");
        return new ResponseEntity<>(Collections.singletonList(response), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
