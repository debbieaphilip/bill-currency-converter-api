package com.company.bill.currencyconverter.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRate {

    String currency;
    double rate;

}
