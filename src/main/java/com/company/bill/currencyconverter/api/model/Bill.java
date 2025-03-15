package com.company.bill.currencyconverter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Bill {
    @JsonProperty("items")
    List<Item> items;
    @JsonProperty
    Customer customer;
    @JsonProperty
    String originalCurrency;
}
