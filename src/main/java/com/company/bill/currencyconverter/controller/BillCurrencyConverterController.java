package com.company.bill.currencyconverter.controller;

import com.company.bill.currencyconverter.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.service.BillCurrencyConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableCaching
@RequiredArgsConstructor
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BillCurrencyConverterController {

    private final BillCurrencyConverterService billCurrencyConverterService;


    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BillCurrencyConverterResponse> calculatePayableAmountInTargetCurrency(
            @RequestBody BillCurrencyConverterRequest request) {
        return ResponseEntity.ok(billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));

    }

}
