package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.api.ExchangeRateApi;
import com.company.bill.currencyconverter.exception.ThirdPartyApiException;
import com.company.bill.currencyconverter.model.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExchangeRateConverterServiceTest {

    @InjectMocks
    private ExchangeRateConverterService exchangeRateConverterService;
    @Mock
    private ExchangeRateApi exchangeRateApi;
    private ExchangeRate exchangeRate;

    String baseCurrency;

    @BeforeEach
    void setUp() {
        baseCurrency = "AED";
        String targetCurrency = "GBP";
        MockitoAnnotations.openMocks(this);
        exchangeRate = new ExchangeRate(targetCurrency, BigDecimal.valueOf(0.21));
        List<ExchangeRate> exchangeRates = List.of(exchangeRate);
        when(exchangeRateApi.loadExchangeRatesForBaseCurrency(baseCurrency)).thenReturn(exchangeRates);
    }

    @Test
    void testGetExchangeRateForBaseCurrencyAndTargetCurrencyIsSuccessful() {
        String targetCurrency = "GBP";
        ExchangeRate result = exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
        assertNotNull(result);
        assertEquals(result, exchangeRate);
        verify(exchangeRateApi, times(1)).loadExchangeRatesForBaseCurrency(baseCurrency);
        verify(exchangeRateApi, times(0)).loadExchangeRatesForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);

    }

    @Test
    void testGetExchangeRateForBaseCurrencyAndTargetCurrencyNotFound() {
        String targetCurrency = "EUR";

        assertThrows(ThirdPartyApiException.class, () -> exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency));

    }

}
