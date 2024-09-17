package com.example.demo.service.impl;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.provider.CurrencyRateProvider;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final Map<String, CurrencyRateProvider> providers;

    @Value("${provider.name}")
    private String providerName;

    @Override
    public Iterable<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency addCurrency(String jsonData) {
        CurrencyRateProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new RuntimeException("Provider not found: " + providerName);
        }

        return currencyRepository.save(provider.getCurrency(jsonData));
    }

    @Override
    public List<ExchangeRate> getExchangeRate(String baseCurrencyCode) {
        log.info("CurrencyService getExchangeRate by baseCurrency: {}", baseCurrencyCode);

        var currency =  currencyRepository.findExchangeRate(baseCurrencyCode);

        return Objects.nonNull(currency) ? currency.getExchangeRates(): List.of();
    }

    @Override
    public void deleteCurrency(Currency currency) {
        currencyRepository.delete(currency);
    }

    @Override
    @Scheduled(fixedDelay = 5, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @CacheEvict(value = {"currency"}, allEntries = true)
    public void cacheEvict() {
    }
}
