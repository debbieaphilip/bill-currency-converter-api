package com.company.bill.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    @JsonProperty
    String name;
    @JsonProperty
    String customerType;
    @JsonProperty
    int tenure;
}
