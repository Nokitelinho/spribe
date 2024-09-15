package com.example.demo;

import com.example.demo.controller.CurrencyController;
import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.service.CurrencyService;
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

    @Test
    public void testCacheableGetExchangeRate() {

        // given
        var exchangeRateList = List.of(new ExchangeRate());

        // when
        when(currencyService.getExchangeRate("GBP")).thenReturn(exchangeRateList);

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