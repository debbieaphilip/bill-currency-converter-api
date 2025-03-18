package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.exception.BillCurrencyConverterApiException;
import com.company.bill.currencyconverter.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BillCurrencyConverterServiceTest {

    @MockBean
    ExchangeRateConverterService exchangeRateConverterService;
    @Autowired
    BillCurrencyConverterService billCurrencyConverterService;
    @Autowired
    DiscountCalculationService discountCalculationService;
    private Customer customer;
    private static final String AED_CURRENCY = "AED";
    private static final String GBP_CURRENCY = "GBP";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer("TestCustomer", CustomerType.CUSTOMER.toString(), 3);

        when(exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(AED_CURRENCY, GBP_CURRENCY))
                .thenReturn(new ExchangeRate(GBP_CURRENCY, new BigDecimal("0.21")));
    }

    @Test
    void calculatePayableAmountInTargetCurrencyForGroceries() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(2).price(BigDecimal.valueOf(50.75)).category(ItemCategory.GROCERIES.toString()).build());
        items.add(Item.builder().name("item2").quantity(1).price(BigDecimal.valueOf(25)).category(ItemCategory.GROCERIES.toString()).build());
        items.add(Item.builder().name("item3").quantity(5).price(BigDecimal.valueOf(1.50)).category(ItemCategory.GROCERIES.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(AED_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        Assertions.assertEquals(new BillCurrencyConverterResponse(BigDecimal.valueOf(27.09), GBP_CURRENCY), billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));
    }

    @Test
    void calculatePayableAmountInTargetCurrencyForGroceriesWithFlatDiscount() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(2).price(BigDecimal.valueOf(450.75)).category(ItemCategory.GROCERIES.toString()).build());
        items.add(Item.builder().name("item2").quantity(1).price(BigDecimal.valueOf(25)).category(ItemCategory.GROCERIES.toString()).build());
        items.add(Item.builder().name("item3").quantity(5).price(BigDecimal.valueOf(1.50)).category(ItemCategory.GROCERIES.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(AED_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        Assertions.assertEquals(new BillCurrencyConverterResponse(BigDecimal.valueOf(186.69), GBP_CURRENCY), billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));
    }

    @Test
    void calculatePayableAmountInTargetCurrencyForNonGroceriesWithFlatDiscount() {

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(450.75)).category(ItemCategory.ELECTRONICS.toString()).build());
        items.add(Item.builder().name("item2").quantity(2).price(BigDecimal.valueOf(25)).category(ItemCategory.MISCELLANEOUS.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(AED_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        Assertions.assertEquals(new BillCurrencyConverterResponse(BigDecimal.valueOf(94.65), GBP_CURRENCY), billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));


    }

    @Test
    void calculatePayableAmountInTargetCurrencyForNonGroceries() {

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(50.75)).category(ItemCategory.ELECTRONICS.toString()).build());
        items.add(Item.builder().name("item2").quantity(1).price(BigDecimal.valueOf(25)).category(ItemCategory.MISCELLANEOUS.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(AED_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        Assertions.assertEquals(new BillCurrencyConverterResponse(BigDecimal.valueOf(15.11), GBP_CURRENCY), billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));
    }

    @ParameterizedTest
    @CsvSource({
            "'item1', 50.75, 'ELECTRONICS', 'item2', 25, 'GROCERIES', 15.37", //test for both categories
            "'item1', 50.75, 'ELECTRONICS', 'item2', 125, 'GROCERIES', 35.32", //test for both categories with grocery eligible for flat discount
            "'item1', 150.75, 'ELECTRONICS', 'item2', 125, 'GROCERIES', 54.22" //test for both categories eligible for flat discount
    })
    void calculatePayableAmountInTargetCurrencyForBothCategories(
            String item1Name, double item1Price, String item1Category,
            String item2Name, double item2Price, String item2Category,
            double expectedAmount) {

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .name(item1Name)
                .quantity(1)
                .price(BigDecimal.valueOf(item1Price))
                .category(ItemCategory.valueOf(item1Category).toString())
                .build());

        items.add(Item.builder()
                .name(item2Name)
                .quantity(1)
                .price(BigDecimal.valueOf(item2Price))
                .category(ItemCategory.valueOf(item2Category).toString())
                .build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(AED_CURRENCY)
                .build();


        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);

        Assertions.assertEquals(
                new BillCurrencyConverterResponse(BigDecimal.valueOf(expectedAmount), GBP_CURRENCY),
                billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request)
        );
    }

    @Test
    void calculatePayableAmountIfTargetAndBaseCurrenciesAreSame() {

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(150.75)).category(ItemCategory.ELECTRONICS.toString()).build());
        items.add(Item.builder().name("item2").quantity(1).price(BigDecimal.valueOf(125)).category(ItemCategory.GROCERIES.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(GBP_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);

        Assertions.assertEquals(new BillCurrencyConverterResponse(BigDecimal.valueOf(258.21), GBP_CURRENCY), billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));
    }

    @Test
    void testThrowsBillCurrencyConverterApiExceptionIfTargetCurrencyIsBlank() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(150.75)).category(ItemCategory.ELECTRONICS.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(GBP_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, null);
        assertThatExceptionOfType(BillCurrencyConverterApiException.class)
                .isThrownBy(() -> billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request))
                .withMessage("Target currency not provided");

    }

    @Test
    void testThrowsBillCurrencyConverterApiExceptionIfBaseCurrencyIsBlank() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(150.75)).category(ItemCategory.ELECTRONICS.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(null)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        assertThatExceptionOfType(BillCurrencyConverterApiException.class)
                .isThrownBy(() -> billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request))
                .withMessage("Original currency not provided");
    }

    @Test
    void testThrowsBillCurrencyConverterApiExceptionIfNoItemsInBill() {
        List<Item> items = new ArrayList<>();

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(GBP_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        assertThatExceptionOfType(BillCurrencyConverterApiException.class)
                .isThrownBy(() -> billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request))
                .withMessage("No items in bill");
    }

    @Test
    void testThrowsBillCurrencyConverterApiExceptionIfTotalPriceIsZero() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(0)).category(ItemCategory.ELECTRONICS.toString()).build());

        var bill = Bill.builder()
                .customer(customer)
                .items(items)
                .originalCurrency(GBP_CURRENCY)
                .build();
        var request = new BillCurrencyConverterRequest(bill, GBP_CURRENCY);
        assertThatExceptionOfType(BillCurrencyConverterApiException.class)
                .isThrownBy(() -> billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request))
                .withMessage("Bill amount is 0");

    }
}
