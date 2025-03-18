package com.company.bill.currencyconverter.api;

import com.company.bill.currencyconverter.client.ExchangeRateApiClient;
import com.company.bill.currencyconverter.dto.ExchangeRateResponse;
import com.company.bill.currencyconverter.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExchangeRateApi {

    @Value("${api-key}")
    private String apiKey;
    private final ExchangeRateApiClient exchangeRateApiClient;
    private final CacheManager cacheManager;

    public ExchangeRateApi(ExchangeRateApiClient exchangeRateApiClient,CacheManager cacheManager){
        this.exchangeRateApiClient = exchangeRateApiClient;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "exchangeRatesForBaseCurrency")
    public List<ExchangeRate> loadExchangeRatesForBaseCurrency(String baseCurrency) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        ExchangeRateResponse response = exchangeRateApiClient.getExchangeRates(apiKey, baseCurrency);
        if (!response.getConversionRateMap().isEmpty()) {
            exchangeRates = response.getConversionRateMap().entrySet().stream().map(entry -> new ExchangeRate(entry.getKey(), entry.getValue())).toList();

        }

        return exchangeRates;
    }

    @Cacheable(value = "exchangeRatesForBaseCurrencyAndTargetCurrency")
    public ExchangeRate loadExchangeRatesForBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency) {
        ExchangeRateResponse response = exchangeRateApiClient.getExchangeRatesForTargetCurrency(apiKey, baseCurrency, targetCurrency);
        return new ExchangeRate(targetCurrency, new BigDecimal(response.getConversionRate()));
    }

    @Scheduled(fixedRate = 86400000) //evict cache after a day 86400000
    public void evictCaches(){
        Objects.requireNonNull(cacheManager.getCache("exchangeRatesForBaseCurrency")).clear();
        Objects.requireNonNull(cacheManager.getCache("exchangeRatesForBaseCurrencyAndTargetCurrency")).clear();
    }

}
