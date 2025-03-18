package com.company.bill.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

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
