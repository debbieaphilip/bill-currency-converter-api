package com.company.bill.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemCategory {
    GROCERIES("Groceries"),
    ELECTRONICS("Electronics"),
    MISCELLANEOUS("Miscellaneous");

    private final String value;

    ItemCategory(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
