package com.spribe.domain;

import com.spribe.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CurrencyTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    private Currency currency;

    @BeforeEach
    public void setUp() {
        currency = new Currency();
        currency.setSuccess(true);
        currency.setTimestamp(System.currentTimeMillis());
        currency.setBaseCurrency("USD");
        currency.setDate(LocalDate.now());
        currency.setRates(Collections.emptyList());

        currencyRepository.deleteAll();
    }

    @Test
    public void testSaveCurrency() {
        Currency savedCurrency = currencyRepository.save(currency);

        assertNotNull(savedCurrency.getId());
        assertEquals(currency.getBaseCurrency(), savedCurrency.getBaseCurrency());
        assertEquals(currency.getTimestamp(), savedCurrency.getTimestamp());
        assertEquals(currency.getDate(), savedCurrency.getDate());
    }

    @Test
    public void testFindCurrencyByBaseCurrency() {
        currencyRepository.save(currency);

        Currency foundCurrency = currencyRepository.findByBaseCurrencyCode("USD");

        assertNotNull(foundCurrency);
        assertEquals(currency.getBaseCurrency(), foundCurrency.getBaseCurrency());
    }
}