package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.api.ExchangeRateApi;
import com.company.bill.currencyconverter.exception.ThirdPartyApiException;
import com.company.bill.currencyconverter.model.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExchangeRateConverterService {

    private ExchangeRateApi exchangeRateApi;

    public ExchangeRate getExchangeRateForBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency) {

        List<ExchangeRate> listOfExchangeRates = exchangeRateApi.loadExchangeRatesForBaseCurrency(baseCurrency);

        if(listOfExchangeRates.isEmpty()){
            throw new ThirdPartyApiException("No exchange rates found for this currency: " + baseCurrency);
        }

        //fetch baseCurrency->targetCurrency exchange rate mapping
        Optional<ExchangeRate> exchangeRate = listOfExchangeRates.stream().filter(er -> targetCurrency.equalsIgnoreCase(er.getCurrency())).findFirst();

        //if the listOfExchangeRates was cached and the requested base->target exchange currency rate is not present, then call API for the requested target currency
        if(exchangeRate.isEmpty()){
            exchangeRate = Optional.ofNullable(exchangeRateApi.loadExchangeRatesForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency));
        }

        return exchangeRate.orElseThrow(() -> new ThirdPartyApiException("No exchange rates found for base currency: " + baseCurrency + " and target currency: " + targetCurrency));

    }
}
