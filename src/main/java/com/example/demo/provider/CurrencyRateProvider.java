package com.example.demo.provider;

import com.example.demo.domain.Currency;

public interface CurrencyRateProvider {
    Currency getCurrency(String jsonData);
}
