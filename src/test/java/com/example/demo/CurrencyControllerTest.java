package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class CurrencyControllerTest {

    @SpyBean
    private CurrencyService currencyService;

    @Autowired
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCacheableGetExchangeRate() {

        // given
        when(currencyService.getExchangeRate("GBP")).thenReturn(List.of(new ExchangeRate()));

        // when
        currencyController.getExchangeRate("GBP");
        currencyController.getExchangeRate("GBP");

        // then
        verify(currencyService, times(1)).getExchangeRate("GBP");

    }

    @Test
    public void testCacheableGetAllCurrencies() {
        // given
        List<Currency> currencyList = List.of(new Currency());

        // when
        when(currencyService.getAllCurrencies()).thenReturn(currencyList);

        currencyController.getAllCurrencies();
        currencyController.getAllCurrencies();

        // then
        verify(currencyService, times(1)).getAllCurrencies();

    }
}