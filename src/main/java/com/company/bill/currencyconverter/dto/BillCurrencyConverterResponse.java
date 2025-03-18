package com.company.bill.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BillCurrencyConverterResponse {

    //net payable amount
    BigDecimal totalPayableAmount;
    //currency
    String currency;
}
