package com.example.demo.provider;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("providerExchangeratesapi")
public class ProviderExchangeratesapi implements CurrencyRateProvider {

    @Override
    public Currency getCurrency(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);

            Currency currency = new Currency();
            currency.setSuccess(rootNode.get("success").asBoolean());
            currency.setTimestamp(rootNode.get("timestamp").asLong());
            currency.setBaseCurrency(rootNode.get("base").asText());
            currency.setDate(LocalDate.parse(rootNode.get("date").asText()));

            List<ExchangeRate> rateList = new ArrayList<>();

            JsonNode ratesNode = rootNode.get("rates");
            ratesNode.fieldNames().forEachRemaining(currencyCode -> {
                ExchangeRate rate = new ExchangeRate();
                rate.setCurrencyCode(currencyCode);
                rate.setRate(ratesNode.get(currencyCode).asDouble());
                rateList.add(rate);
            });

            currency.setExchangeRates(rateList);

            return currency;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing data for ProviderA", e);
        }
    }
}
