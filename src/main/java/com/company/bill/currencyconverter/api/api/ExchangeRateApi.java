package com.company.bill.currencyconverter.api.api;

import com.company.bill.currencyconverter.api.client.ExchangeRateApiClient;
import com.company.bill.currencyconverter.api.dto.ExchangeRateResponse;
import com.company.bill.currencyconverter.api.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExchangeRateApi {

    @Autowired
    private ExchangeRateApiClient exchangeRateApiClient;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value="exchangeRatesForBaseCurrency")
    public List<ExchangeRate> loadExchangeRatesForBaseCurrency(String baseCurrency){
        String apiKey = "765deab46a3338e56d10483c";
        List<ExchangeRate> exchangeRates =  new ArrayList<>();
        ExchangeRateResponse response = exchangeRateApiClient.getExchangeRates(apiKey,baseCurrency);
        if(!response.getConversionRateMap().isEmpty()){
            exchangeRates = response.getConversionRateMap().entrySet().stream().map(entry->new ExchangeRate(entry.getKey(), entry.getValue())).toList();

        }

        return exchangeRates;
    }

    @Cacheable(value="exchangeRatesForBaseCurrencyAndTargetCurrency")
    public ExchangeRate loadExchangeRatesForBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency){
        String apiKey = "765deab46a3338e56d10483c";
        ExchangeRateResponse response = exchangeRateApiClient.getExchangeRatesForTargetCurrency(apiKey,baseCurrency,targetCurrency);
        return new ExchangeRate(targetCurrency,Double.parseDouble(response.getConversionRate()));
    }

    @Scheduled(fixedRate = 86400000) //evict cache after a day
    public void evictCaches(){
        Objects.requireNonNull(cacheManager.getCache("exchangeRatesForBaseCurrency")).clear();
        Objects.requireNonNull(cacheManager.getCache("exchangeRatesForBaseCurrencyAndTargetCurrency")).clear();
    }

}
