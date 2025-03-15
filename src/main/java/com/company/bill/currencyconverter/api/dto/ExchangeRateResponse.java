package com.company.bill.currencyconverter.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    @JsonProperty("result")
    private String result;
    @JsonProperty("base_code")
    private String baseCode;
    @JsonProperty("target_code")
    private String targetCode;
    @JsonProperty("conversion_rates")
    private Map<String,Double> conversionRateMap;
    @JsonProperty("conversion_rate")
    private String conversionRate;


}
