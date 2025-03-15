package com.company.bill.currencyconverter.api.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse{
    HttpStatus status;
    String message;

    public ErrorResponse(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
