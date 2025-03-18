package com.company.bill.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerType {
    EMPLOYEE("EMPLOYEE"),
    AFFILIATE("AFFILIATE"),
    CUSTOMER("CUSTOMER");

    private final String value;

    CustomerType(String value){
        this.value=value;
    }

    @JsonValue
    @Override
    public String toString(){
        return value;
    }

}
