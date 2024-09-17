package com.example.demo.provider;

import com.example.demo.domain.Currency;
import org.springframework.stereotype.Component;

@Component("providerB")
public class ProviderB implements CurrencyRateProvider {

    @Override
    public Currency getCurrency(String jsonData) {
        // Implement parsing logic for ProviderB
        return new Currency();
    }
}
