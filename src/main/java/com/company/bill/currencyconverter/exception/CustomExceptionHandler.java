package com.company.bill.currencyconverter.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ThirdPartyApiException.class})
    public ErrorResponse handleThirdPartyException(ThirdPartyApiException ex){
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid response received from API");
    }

    @ResponseBody
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignRunException(FeignException ex) {
        String errorResponseMessage = ex.getMessage();
        HttpStatus errorResponseStatus = HttpStatus.resolve(ex.status());
        ErrorResponse errorResponse;

        if(errorResponseStatus == null)
            errorResponse = new ErrorResponse(HttpStatus.BAD_GATEWAY,"Unable to connect");
        else if(HttpStatus.NOT_FOUND.equals(errorResponseStatus)){
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,"Invalid currency code");
        }
        else
            errorResponse = new ErrorResponse(errorResponseStatus,errorResponseMessage);

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllOthers(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }


}
