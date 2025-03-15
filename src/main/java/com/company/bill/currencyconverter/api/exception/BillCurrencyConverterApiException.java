package com.company.bill.currencyconverter.api.exception;

public class BillCurrencyConverterApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BillCurrencyConverterApiException(String msg){
        super(msg);
    }
    public BillCurrencyConverterApiException(String msg, Throwable t){
        super(msg,t);
    }

}
