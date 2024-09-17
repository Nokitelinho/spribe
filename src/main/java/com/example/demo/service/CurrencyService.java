package com.example.demo.service;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.dto.CurrencyDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CurrencyService {
    Iterable<Currency> getAllCurrencies();

    Currency addCurrency(CurrencyDTO currencyDTO);

    List<ExchangeRate> getExchangeRate(String baseCurrencyCode);

    void deleteCurrency(Currency currency);

    @Scheduled(fixedDelay = 5, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @CacheEvict(value = {"currency"}, allEntries = true)
    void cacheEvict();
}
