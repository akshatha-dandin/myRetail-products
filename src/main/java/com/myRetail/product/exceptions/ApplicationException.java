package com.myRetail.product.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private String errorMessage;
    private String responseStatusCode;
    private HttpStatus httpStatus;

    public ApplicationException(String errorMessage, String responseStatusCode, HttpStatus httpStatus) {
        super("ApplicationException Occurred");
        this.errorMessage = errorMessage;
        this.responseStatusCode = responseStatusCode;
        this.httpStatus = httpStatus;
    }

    public ApplicationException(String errorMessage) {
        super("ApplicationException Occurred");
        this.errorMessage = errorMessage;
        this.responseStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.toString();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
