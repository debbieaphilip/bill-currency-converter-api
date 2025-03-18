package com.company.bill.currencyconverter.dto;

import com.company.bill.currencyconverter.model.Bill;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillCurrencyConverterRequest {
    @JsonProperty("bill")
    private Bill bill;
    @JsonProperty("targetCurrency")
    private String targetCurrency;

}
