package com.example.demo;

import com.example.demo.controller.CurrencyController;
import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.*;

class CurrencyControllerTest extends AbstractTest{

    @SpyBean
    private CurrencyController currencyController;

    @MockBean
    private CurrencyService currencyService;


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