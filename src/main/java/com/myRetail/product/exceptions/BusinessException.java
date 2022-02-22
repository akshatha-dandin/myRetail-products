package com.myRetail.product.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private String errorMessage;
    private String responseStatusCode;
    private HttpStatus httpStatus;

    public BusinessException(String errorMessage, String responseStatusCode, HttpStatus httpStatus) {
        super("Business Exception Occurred");
        this.errorMessage = errorMessage;
        this.responseStatusCode = responseStatusCode;
        this.httpStatus = httpStatus;
    }
}
