package com.company.bill.currencyconverter.service;

import com.company.bill.currencyconverter.model.Customer;
import com.company.bill.currencyconverter.model.CustomerType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DiscountCalculationService {

    @Value("${discount.flatAmount}")
    private String flatDiscountAmount;
    @Value("${discount.employee}")
    private String employeeDiscountPercentage;
    @Value("${discount.affiliate}")
    private String affiliateDiscountPercentage;
    @Value("${discount.customer}")
    private String customerDiscountPercentage;

    private static final int MIN_CUSTOMER_TENURE = 2;
    private static final int SCALE = 2;
    private final Map<String, BigDecimal> percentageDiscountMap = new ConcurrentHashMap<>();

    @PostConstruct
    void init(){
        percentageDiscountMap.put(CustomerType.EMPLOYEE.toString(), new BigDecimal(employeeDiscountPercentage));
        percentageDiscountMap.put(CustomerType.AFFILIATE.toString(), new BigDecimal(affiliateDiscountPercentage));
        percentageDiscountMap.put(CustomerType.CUSTOMER.toString(), new BigDecimal(customerDiscountPercentage));
    }

    public BigDecimal calculateDiscount(Customer customer, BigDecimal amount) {

        //get discount percentage as per customer type
        var percentage = getCustomerDiscountPercentage(customer);

        //calculate discount
        BigDecimal discount = calculateDiscountPercentage(amount, percentage);

        //add flat rate if applicable
        return discount.add(getFlatDiscountRate(amount));
    }

    private BigDecimal getCustomerDiscountPercentage(Customer customer) {
        if (CustomerType.CUSTOMER.toString().equalsIgnoreCase(customer.getCustomerType())) {
            //Customer discount is applied only if customer tenure > min tenure
            if (customer.getTenure() > MIN_CUSTOMER_TENURE) {
                return percentageDiscountMap.get(CustomerType.CUSTOMER.toString());
            } else return BigDecimal.ZERO;
        }

        //get discount rate for other customer types
        return percentageDiscountMap.getOrDefault(customer.getCustomerType().toUpperCase(), BigDecimal.ZERO);

    }

    private BigDecimal calculateDiscountPercentage(BigDecimal amount, BigDecimal percentage) {

        if (percentage.compareTo(BigDecimal.ZERO) > 0)
            return amount.multiply(percentage).divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);

        return BigDecimal.ZERO;
    }

    public BigDecimal getFlatDiscountRate(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return amount.divideToIntegralValue(BigDecimal.valueOf(100)).multiply(new BigDecimal(flatDiscountAmount));
        }

        return BigDecimal.ZERO;
    }

}
