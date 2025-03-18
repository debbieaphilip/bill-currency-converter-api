package com.company.bill.currencyconverter.api;

import com.company.bill.currencyconverter.client.ExchangeRateApiClient;
import com.company.bill.currencyconverter.dto.ExchangeRateResponse;
import com.company.bill.currencyconverter.model.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateResponseTest {
    @InjectMocks
    private ExchangeRateApi exchangeRateApi;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    @Value("${api-key}")
    private String apiKey;


    @Test
    void testLoadExchangeRatesForBaseCurrency() {
        String baseCurrency = "USD";
        ExchangeRateResponse mockResponse = mock(ExchangeRateResponse.class);
        when(mockResponse.getConversionRateMap())
                .thenReturn(Map.of("EUR", BigDecimal.valueOf(0.85)));
        when(exchangeRateApiClient.getExchangeRates(apiKey, baseCurrency)).thenReturn(mockResponse);

        List<ExchangeRate> rates = exchangeRateApi.loadExchangeRatesForBaseCurrency(baseCurrency);

        assertNotNull(rates);
        assertEquals(1, rates.size());
        assertEquals("EUR", rates.getFirst().getCurrency());
        assertEquals(new BigDecimal("0.85"), rates.getFirst().getRate());
    }

}
