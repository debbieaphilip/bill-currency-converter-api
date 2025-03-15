package com.company.bill.currencyconverter.api.controller;

import com.company.bill.currencyconverter.api.dto.BillCurrencyConverterRequest;
import com.company.bill.currencyconverter.api.dto.BillCurrencyConverterResponse;
import com.company.bill.currencyconverter.api.service.BillCurrencyConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@EnableCaching
@RequiredArgsConstructor
@RequestMapping(path="/api",produces = {MediaType.APPLICATION_JSON_VALUE})
public class BillCurrencyConverterController {

    private final BillCurrencyConverterService billCurrencyConverterService;


    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BillCurrencyConverterResponse> calculatePayableAmountInTargetCurrency(URI uri,
                                                       @RequestHeader Map<String, String> headerMap,
                                                       @RequestBody BillCurrencyConverterRequest request){
        return ResponseEntity.ok(billCurrencyConverterService.calculatePayableAmountInTargetCurrency(request));

    }

    //TODO add config files for percentages and api key and security
    //TODO unit tests and generate code coverage
    //TODO readme
    //TODO code quality sonarqube
    //TODO linting
    //TODO caching?
    //TODO github --do this first to check if we can upload to github



}
