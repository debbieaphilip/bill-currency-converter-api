package com.company.bill.currencyconverter.api.service;

import com.company.bill.currencyconverter.api.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.api.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.api.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class BillCurrencyConverterService {

    private ExchangeRateConverterService exchangeRateConverterService;
    private DiscountCalculationService discountCalculationService;

    public BillCurrencyConverterResponse calculatePayableAmountInTargetCurrency(BillCurrencyConverterRequest request){
        var bill = request.getBill();
        var items = bill.getItems();
        var totalAmount = calculateTotalAmount(bill.getItems());
        var targetCurrency = request.getTargetCurrency();
        var exchangeRate = getExchangeRate(bill.getOriginalCurrency(),targetCurrency);
        var amountInTargetCurrency = totalAmount.multiply(BigDecimal.valueOf(exchangeRate.getRate())).setScale(2, RoundingMode.HALF_UP);
        var totalAmountForNonGroceryItemsInTargetCurrency = totalAmount;

        var nonGroceryItems = items.stream().filter(i->!ItemCategory.GROCERIES.toString().equalsIgnoreCase(i.getCategory())).toList();
        if(nonGroceryItems.size() != items.size())
            totalAmountForNonGroceryItemsInTargetCurrency = calculateTotalAmount(nonGroceryItems).multiply(BigDecimal.valueOf(exchangeRate.getRate())).setScale(2, RoundingMode.HALF_UP);

        //excluding groceries in the discount calculation
        var discount = discountCalculationService.calculateDiscount(bill.getCustomer(), totalAmountForNonGroceryItemsInTargetCurrency, targetCurrency);
        var totalPayableAmount = amountInTargetCurrency.subtract(discount);
        return new BillCurrencyConverterResponse(totalPayableAmount,targetCurrency);

    }

    private BigDecimal calculateTotalAmount(List<Item> items){
        return items.stream().map(i->i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

    }

    private ExchangeRate getExchangeRate(String baseCurrency, String targetCurrency){
        return exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
    }

}
