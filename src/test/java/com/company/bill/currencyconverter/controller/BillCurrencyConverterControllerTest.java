package com.company.bill.currencyconverter.controller;

import com.company.bill.currencyconverter.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.model.Bill;
import com.company.bill.currencyconverter.model.Customer;
import com.company.bill.currencyconverter.model.CustomerType;
import com.company.bill.currencyconverter.model.Item;
import com.company.bill.currencyconverter.service.BillCurrencyConverterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class BillCurrencyConverterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillCurrencyConverterService billCurrencyConverterService;

    @InjectMocks
    private BillCurrencyConverterController billCurrencyConverterController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(billCurrencyConverterController).build();
        objectMapper = new ObjectMapper();
    }

    private ObjectMapper objectMapper;

    @Test
    void testCalculatePayableAmountInTargetCurrencySuccess() throws Exception {
        var itemList = new ArrayList<Item>();
        itemList.add(Item.builder().name("item1").quantity(1).price(BigDecimal.valueOf(2)).category("GROCERIES").build());
        BillCurrencyConverterRequest request = new BillCurrencyConverterRequest(Bill.builder()
                .originalCurrency("USD")
                .items(itemList)
                .customer(new Customer("customer1", CustomerType.CUSTOMER.toString(), 1))
                .build(), "EUR");

        BillCurrencyConverterResponse response = new BillCurrencyConverterResponse(BigDecimal.valueOf(85), "EUR");

        when(billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request))
                .thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPayableAmount").value(85.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testCalculatePayableAmountInTargetCurrencyFailure() throws Exception {

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}