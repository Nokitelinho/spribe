package com.example.demo.service;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.mappers.CurrencyMapper;
import com.example.demo.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public Iterable<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency addCurrency(CurrencyDTO currencyDTO) {
        var currency = currencyMapper.dtoToEntity(currencyDTO);

        return currencyRepository.save(currency);
    }

    public List<ExchangeRate> getExchangeRate(String baseCurrencyCode) {
        var currency =  currencyRepository.findExchangeRate(baseCurrencyCode);

        return Objects.nonNull(currency) ? currency.getExchangeRates(): List.of();
    }

    public void deleteCurrency(Currency currency) {
        currencyRepository.delete(currency);
    }

    @Scheduled(fixedDelay = 5, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @CacheEvict(value = {"currency"}, allEntries = true)
    public void cacheEvict() {
    }
}
