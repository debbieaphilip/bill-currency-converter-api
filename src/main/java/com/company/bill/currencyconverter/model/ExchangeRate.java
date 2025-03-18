package com.company.bill.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRate {

    String currency;
    BigDecimal rate;

}
