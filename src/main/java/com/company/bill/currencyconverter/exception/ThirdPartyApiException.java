package com.company.bill.currencyconverter.exception;

import java.io.Serial;

public class ThirdPartyApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ThirdPartyApiException(String msg) {
        super(msg);
    }

}
