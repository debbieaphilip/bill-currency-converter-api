package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.exception.BillCurrencyConverterApiException;
import com.company.bill.currencyconverter.model.ExchangeRate;
import com.company.bill.currencyconverter.model.Item;
import com.company.bill.currencyconverter.model.ItemCategory;
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

    public BillCurrencyConverterResponse calculatePayableAmountInTargetCurrency(BillCurrencyConverterRequest request) {
        var bill = request.getBill();
        var items = bill.getItems();
        var totalAmount = calculateTotalAmount(bill.getItems());

        var targetCurrency = request.getTargetCurrency();
        var baseCurrency = request.getBill().getOriginalCurrency();

        if (targetCurrency == null || targetCurrency.isEmpty())
            throw new BillCurrencyConverterApiException("Target currency not provided");
        if (baseCurrency == null || baseCurrency.isEmpty())
            throw new BillCurrencyConverterApiException("Original currency not provided");

        if (items.isEmpty())
            throw new BillCurrencyConverterApiException("No items in bill");
        if (BigDecimal.ZERO.compareTo(totalAmount) == 0)
            throw new BillCurrencyConverterApiException("Bill amount is 0");

        BigDecimal nonGroceryItemsAmount;
        BigDecimal groceryItemsAmount;
        BigDecimal nonGroceryDiscount;
        BigDecimal groceryDiscount;

        var nonGroceryItems = filterNonGroceryItems(items);

        //no grocery items
        if (nonGroceryItems.size() == items.size()) {
            nonGroceryItemsAmount = calculateTotalAmount(nonGroceryItems);
            nonGroceryDiscount = discountCalculationService.calculateDiscount(bill.getCustomer(), nonGroceryItemsAmount);
            groceryDiscount = BigDecimal.ZERO;
        } else if (!nonGroceryItems.isEmpty()) {
            nonGroceryItemsAmount = calculateTotalAmount(nonGroceryItems);
            groceryItemsAmount = totalAmount.subtract(nonGroceryItemsAmount);
            //excluding groceries in the percentage discount calculation
            nonGroceryDiscount = discountCalculationService.calculateDiscount(bill.getCustomer(), nonGroceryItemsAmount);
            groceryDiscount = discountCalculationService.getFlatDiscountRate(groceryItemsAmount);

        }
        //no non-grocery items
        else {
            groceryItemsAmount = totalAmount;
            nonGroceryDiscount = BigDecimal.ZERO;
            groceryDiscount = discountCalculationService.getFlatDiscountRate(groceryItemsAmount);
        }

        var discount = nonGroceryDiscount.add(groceryDiscount);
        var totalPayableAmount = totalAmount.subtract(discount);

        //if baseCurrency and targetCurrency are the same, return amount without conversion
        if (baseCurrency.equalsIgnoreCase(targetCurrency)) {
            return new BillCurrencyConverterResponse(totalPayableAmount, targetCurrency);
        }

        var exchangeRate = getExchangeRate(bill.getOriginalCurrency(), targetCurrency);
        var amountInTargetCurrency = totalPayableAmount.multiply(exchangeRate.getRate()).setScale(2, RoundingMode.HALF_UP);

        return new BillCurrencyConverterResponse(amountInTargetCurrency, targetCurrency);

    }

    private List<Item> filterNonGroceryItems(List<Item> items) {
        return items.stream().filter(i -> !ItemCategory.GROCERIES.toString().equalsIgnoreCase(i.getCategory())).toList();
    }

    private BigDecimal calculateTotalAmount(List<Item> items) {
        return items.stream().map(this::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal getItemTotal(Item item) {
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private ExchangeRate getExchangeRate(String baseCurrency, String targetCurrency) {
        return exchangeRateConverterService.getExchangeRateForBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
    }

}
