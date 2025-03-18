package com.company.bill.currencyconverter.exception;

public class ThirdPartyApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ThirdPartyApiException(String msg) {
        super(msg);
    }

}
