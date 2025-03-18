package com.company.bill.currencyconverter.client;

import com.company.bill.currencyconverter.dto.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchangeRateApi", url = "${third-party-api-url}")
public interface ExchangeRateApiClient {

    @GetMapping(value = "/{apiKey}/latest/{currencyId}", produces = "application/json")
    ExchangeRateResponse getExchangeRates(@PathVariable("apiKey") String apiKey,
                                          @PathVariable("currencyId") String currencyId);

    @GetMapping(value = "/{apiKey}/pair/{baseCurrencyId}/{targetCurrencyId}", produces = "application/json")
    ExchangeRateResponse getExchangeRatesForTargetCurrency(@PathVariable String apiKey,
                                                           @PathVariable("baseCurrencyId") String baseCurrencyId,
                                                           @PathVariable("targetCurrencyId") String targetCurrencyId);

}
