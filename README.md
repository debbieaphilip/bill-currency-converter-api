# BILL-CURRENCY-CONVERTER-API

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=debbieaphilip_bill-currency-converter-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=debbieaphilip_bill-currency-converter-api)

## Table of Contents ##    
* [Introduction](https://github.com/TarabutGateway/obc-accounts-api#Introduction)
* [Features](https://github.com/TarabutGateway/obc-accounts-api#Features)
* [Build and Test](https://github.com/TarabutGateway/obc-accounts-api#build-and-test)
* [Configuration](https://github.com/TarabutGateway/obc-accounts-api#configuration)
* [Running The Service Locally](https://github.com/TarabutGateway/obc-accounts-api#running-the-service-locally)
* [Service Liveness Readiness](https://github.com/TarabutGateway/obc-accounts-api#service-liveness-readiness)


## Introduction
bill-currency-converter-api is a service that integrates with a third-party currency
exchange API to retrieve real-time exchange rates. The application calculates the total
payable amount for a bill in a specified currency after applying applicable discounts.

The API endpoint http://localhost:8080/api/calculate allows users to submit a bill in one currency
and get the payable amount in another currency.

## Features

- Java 21
- Spring Boot (version: 3.2.12)
- Open Feign (Declarative REST Clients)

## Build and Test

JDK 21+ (ideally 21) installed.  Repo contains the Gradle wrappers.

Command to build from project home directory

```
./gradlew clean build
```

Command to Run tests

```
./gradlew test
```

## Configuration

The configuration properties are added in 
```
src/main/resources/application.properties
```
This also includes the api-key that is generated to connect to the Third party Exchange Rate API

## Running The Service Locally
First run API : http://localhost:8080/auth/login

This will generate a token. 

This token should be added in the Authorization header as Bearer token in the request http://localhost:8080/api/calculate

* JSON request contains :
  -  bill
  -  targetCurrency.

* bill :
  * customer in the bill is as follows:
      - Customer name
      - Customer Type can be EMPLOYEE, AFFILIATE or CUSTOMER
      - Tenure depicts the customer tenure
   * originalCurrency is the base currency that needs to be converted
   * items is the array of items with their name, category, price and quantity

* targetCurrency is the final currency

Sample JSON request:

{
"bill": {
"customer": {
"name": "CustomerA",
"customerType": "EMPLOYEE",
"tenure": "2"
},
"originalCurrency": "EUR",
"items": [
{
"name": "Item0",
"price": 1,
"quantity": 0,
"category": "ELECTRONIC"
},
{
"name": "Item1",
"price": 2,
"quantity": 1,
"category": "MISC"
},
{
"name": "Item2",
"price": 3,
"quantity": 2,
"category": "GROCERIES"
},
{
"name": "Item3",
"price": 4,
"quantity": 3,
"category": "GROCERIES"
},
{
"name": "Item4",
"price": 5,
"quantity": 4,
"category": "CLOTHES"
}
]
},
"targetCurrency": "EUR"
}
