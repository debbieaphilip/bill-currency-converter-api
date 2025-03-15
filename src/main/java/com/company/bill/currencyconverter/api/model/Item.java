package com.company.bill.currencyconverter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Item {
    @JsonProperty
    String name;
    @JsonProperty
    BigDecimal price;
    @JsonProperty
    int quantity;
    @JsonProperty
    String category;
}
