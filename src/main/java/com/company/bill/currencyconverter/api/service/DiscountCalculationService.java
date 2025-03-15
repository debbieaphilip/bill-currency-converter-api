package com.company.bill.currencyconverter.api.service;

import com.company.bill.currencyconverter.api.model.Customer;
import com.company.bill.currencyconverter.api.model.CustomerType;
import com.company.bill.currencyconverter.api.model.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class DiscountCalculationService {

    @Autowired
    private ExchangeRateConverterService exchangeRateConverterService;
    private static final String USD_CURRENCY = "USD";

    private static final BigDecimal FLAT_DISCOUNT_AMOUNT = new BigDecimal(5);
    private static final BigDecimal EMPLOYEE_DISCOUNT_PERCENTAGE = new BigDecimal(30);
    private static final BigDecimal AFFILIATE_DISCOUNT_PERCENTAGE = new BigDecimal(10);
    private static final BigDecimal CUSTOMER_DISCOUNT_PERCENTAGE = new BigDecimal(5);
    private static final Map<String, BigDecimal> PERCENTAGE_DISCOUNT_MAP = new ConcurrentHashMap<>();

    private DiscountCalculationService(){

        PERCENTAGE_DISCOUNT_MAP.put(CustomerType.EMPLOYEE.toString(), EMPLOYEE_DISCOUNT_PERCENTAGE);
        PERCENTAGE_DISCOUNT_MAP.put(CustomerType.AFFILIATE.toString(), AFFILIATE_DISCOUNT_PERCENTAGE);
        PERCENTAGE_DISCOUNT_MAP.put(CustomerType.CUSTOMER.toString(), CUSTOMER_DISCOUNT_PERCENTAGE);
    }

    public BigDecimal calculateDiscount(Customer customer, BigDecimal amount, String targetCurrency){

        var percentage = BigDecimal.ZERO;
        var discount = BigDecimal.ZERO;


        //if customerType is CUSTOMER, check tenure > 2
        if(CustomerType.CUSTOMER.toString().equalsIgnoreCase(customer.getCustomerType())) {
            if (!StringUtils.isEmpty(customer.getTenure()) && Integer.parseInt(customer.getTenure()) > 2) {
                percentage = PERCENTAGE_DISCOUNT_MAP.get(CustomerType.CUSTOMER);
            }
        }

        //get discount rate for other customer types
        else percentage = PERCENTAGE_DISCOUNT_MAP.getOrDefault(customer.getCustomerType().toUpperCase(),BigDecimal.ZERO);

        //calculate discount
        if(!Objects.equals(percentage, BigDecimal.ZERO))
            discount = amount.multiply(percentage).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);

        //add flat rate if applicable
        return discount.add(getFlatDiscountRate(amount,targetCurrency));
    }

    public BigDecimal getFlatDiscountRate(BigDecimal amount, String targetCurrency) {

        var reverseExchangeRate = new ExchangeRate(USD_CURRENCY,1);
        var amountInUSDCurrency = amount;


        if(!USD_CURRENCY.equalsIgnoreCase(targetCurrency)){
             var exchangeRate = getExchangeRate(targetCurrency,USD_CURRENCY);
             reverseExchangeRate = getExchangeRate(USD_CURRENCY,targetCurrency);
             amountInUSDCurrency = amount.multiply(BigDecimal.valueOf(exchangeRate.getRate())).setScale(2, RoundingMode.HALF_UP);
        }

        if(amountInUSDCurrency.compareTo(BigDecimal.valueOf(100))>=0) {
            BigDecimal discountInUSD = amount.divideToIntegralValue(BigDecimal.valueOf(100)).multiply(FLAT_DISCOUNT_AMOUNT);
            return discountInUSD.multiply(BigDecimal.valueOf(reverseExchangeRate.getRate())).setScale(2, RoundingMode.HALF_UP);

        }

        return BigDecimal.ZERO;
    }

    private ExchangeRate getExchangeRate(String baseCurrency, String targetCurrency){
        return exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
    }

}
