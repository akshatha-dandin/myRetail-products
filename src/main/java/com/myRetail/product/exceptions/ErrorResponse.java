package com.myRetail.product.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;
}
