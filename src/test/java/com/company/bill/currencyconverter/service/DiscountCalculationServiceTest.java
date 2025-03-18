package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.model.Customer;
import com.company.bill.currencyconverter.model.CustomerType;
import com.company.bill.currencyconverter.model.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DiscountCalculationServiceTest {

    @Mock
    ExchangeRateConverterService exchangeRateConverterService;
    @Autowired
    DiscountCalculationService discountCalculationService;

    private Customer employee;
    private Customer affiliate;
    private Customer customer;
    private Customer customerWithTenure;

    private static final String USD_CURRENCY = "USD";
    private static final String AED_CURRENCY = "AED";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Customer("TestCustomer1", CustomerType.EMPLOYEE.toString(), 5);
        affiliate = new Customer("TestCustomer2", CustomerType.AFFILIATE.toString(), 0);
        customer = new Customer("TestCustomer3", CustomerType.CUSTOMER.toString(), 1);
        customerWithTenure = new Customer("TestCustomer4", CustomerType.CUSTOMER.toString(), 3);

        when(exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(AED_CURRENCY, USD_CURRENCY))
                .thenReturn(new ExchangeRate(USD_CURRENCY, new BigDecimal("0.27")));
        when(exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(USD_CURRENCY, AED_CURRENCY))
                .thenReturn(new ExchangeRate(AED_CURRENCY, new BigDecimal("3.67")));

    }

    @Test
    void testCalculateDiscountForEmployee() {
        BigDecimal amount = new BigDecimal(50);

        BigDecimal discount = discountCalculationService.calculateDiscount(employee, amount);

        assertEquals(new BigDecimal("15.00").setScale(2, RoundingMode.HALF_UP), discount);

    }

    @Test
    void testCalculateDiscountForAffiliate() {
        BigDecimal amount = new BigDecimal(50);

        BigDecimal discount = discountCalculationService.calculateDiscount(affiliate, amount);

        assertEquals(new BigDecimal("5").setScale(2, RoundingMode.HALF_UP), discount);

    }

    @Test
    void testCalculateDiscountForCustomerWithTenure() {
        BigDecimal amount = new BigDecimal(50);

        BigDecimal discount = discountCalculationService.calculateDiscount(customerWithTenure, amount);

        assertEquals(new BigDecimal("2.5").setScale(2, RoundingMode.HALF_UP), discount);

    }

    @Test
    void testCalculateDiscountForCustomerWithoutTenure() {
        BigDecimal amount = new BigDecimal(50);

        BigDecimal discount = discountCalculationService.calculateDiscount(customer, amount);

        assertEquals(new BigDecimal(0), discount);

    }

    @Test
    void testCalculateDiscountForFlatDiscount() {
        BigDecimal amount = new BigDecimal(500);

        BigDecimal discount = discountCalculationService.calculateDiscount(customer, amount);

        assertEquals(new BigDecimal("25"), discount);

    }

}
