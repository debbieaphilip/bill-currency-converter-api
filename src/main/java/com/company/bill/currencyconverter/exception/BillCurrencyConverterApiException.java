package com.company.bill.currencyconverter.exception;

import java.io.Serial;

public class BillCurrencyConverterApiException extends RuntimeException{
@Serial
    private static final long serialVersionUID = 1L;

    public BillCurrencyConverterApiException(String msg){
        super(msg);
    }

}
