package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateRequestUrl {
    @Value("${vendor.api.key}")
    private String apiKey;

    @Value("${vendor.base.url}")
    private String baseUrl;

    public String construct(String baseCurrency) {
        return baseUrl + "/v1/latest?access_key=" + apiKey + "&base=" + baseCurrency;
    }
}
