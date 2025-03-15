package com.company.bill.currencyconverter.api.dto;

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
