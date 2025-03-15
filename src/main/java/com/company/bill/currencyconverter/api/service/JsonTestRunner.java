package com.company.bill.currencyconverter.api.service;

import com.company.bill.currencyconverter.api.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.api.model.Bill;
import com.company.bill.currencyconverter.api.model.Customer;
import com.company.bill.currencyconverter.api.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonTestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        BillCurrencyConverterRequest request = new BillCurrencyConverterRequest();
        request.setTargetCurrency("USD");
        List<Item> itemList = new ArrayList<>();
        for(int i=0;i<5;i++){
            itemList.add(Item.builder().name("Item"+i).price(BigDecimal.valueOf(i).add(BigDecimal.ONE)).quantity(i).category("ELECTRONIC").build());
        }
        Bill bill = Bill.builder()
                .items(itemList)
                .customer(new Customer("CustomerA", "EMPLOYEE", "2"))
                .originalCurrency("AED")
                .build();

        request.setBill(bill);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        System.out.println(json);
    }
}
