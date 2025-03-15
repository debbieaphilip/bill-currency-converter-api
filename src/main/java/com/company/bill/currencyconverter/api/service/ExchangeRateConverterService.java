package com.company.bill.currencyconverter.api.service;

import com.company.bill.currencyconverter.api.api.ExchangeRateApi;
import com.company.bill.currencyconverter.api.client.ExchangeRateApiClient;
import com.company.bill.currencyconverter.api.dto.ExchangeRateResponse;
import com.company.bill.currencyconverter.api.exception.ThirdPartyApiException;
import com.company.bill.currencyconverter.api.model.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ExchangeRateConverterService {

    @Autowired
    ExchangeRateApi exchangeRateApi;
    private  final Map<String, List<ExchangeRate>> currencyIdToExchangeRateMapping = new ConcurrentHashMap<>();

    public ExchangeRate getExchangeRateForBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency){
        List<ExchangeRate> listOfExchangeRates = currencyIdToExchangeRateMapping.computeIfAbsent(baseCurrency,
                key->exchangeRateApi.loadExchangeRatesForBaseCurrency(baseCurrency));

        if(listOfExchangeRates.isEmpty()){
            throw new ThirdPartyApiException("No exchange rates found for this currency: "+baseCurrency);
        }

        Optional<ExchangeRate> exchangeRate = listOfExchangeRates.stream().filter(er -> targetCurrency.equalsIgnoreCase(er.getCurrency())).findFirst();

        //if the listOfExchangeRates was cached and the requested currency rate is not present, then call API for the requested currency
        if(exchangeRate.isEmpty()){
            exchangeRate = Optional.ofNullable(exchangeRateApi.loadExchangeRatesForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency));
        }

        return exchangeRate.orElseThrow(()->new ThirdPartyApiException("No exchange rates found for base currency: "+baseCurrency+" and target currency: "+targetCurrency));

    }

    @Scheduled(fixedRate = 86400000) //evict cache after a day
    public void evictCache(){
        Objects.requireNonNull(currencyIdToExchangeRateMapping).clear();
    }
}
