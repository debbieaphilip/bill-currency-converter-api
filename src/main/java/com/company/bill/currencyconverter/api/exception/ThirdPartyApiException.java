package com.company.bill.currencyconverter.api.exception;

public class ThirdPartyApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ThirdPartyApiException(String msg){
        super(msg);
    }
    public ThirdPartyApiException(String msg, Throwable t){
        super(msg,t);
    }

}
