package com.company.bill.currencyconverter.api.dto;

import com.company.bill.currencyconverter.api.model.Bill;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BillCurrencyConverterRequest {
    @JsonProperty("bill")
    private Bill bill;
    @JsonProperty("targetCurrency")
    private String targetCurrency;

}
