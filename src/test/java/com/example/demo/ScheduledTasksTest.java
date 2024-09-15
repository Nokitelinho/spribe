package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledTasksTest {

    @InjectMocks
    ScheduledTasks scheduledTasks;

    @Mock
    CurrencyService currencyService;

    @Mock
    WebClientService webClientService;

    @Mock
    ExchangeRateRequestUrl exchangeRateRequestUrl;

    private Currency populateCurrency() {
        var currency = new Currency();
        currency.setSuccess(true);
        currency.setTimestamp(System.currentTimeMillis());
        currency.setBaseCurrency("GBP");
        currency.setDate(LocalDate.now());

        var exchangeRate = new ExchangeRate();
        exchangeRate.setRate(1.856);
        exchangeRate.setCurrencyCode("JPY");

        currency.setExchangeRates(List.of(exchangeRate));

        return currency;
    }

    @Test
    void verifyTimesForUpdateCurrencyData() {
        // given
        var currencyDTO = new CurrencyDTO();

        // when
        when(currencyService.getAllCurrencies()).thenReturn(List.of(populateCurrency()));
        when(exchangeRateRequestUrl.construct(anyString())).thenReturn("http://example.com");
        when(webClientService.callExternalService(anyString())).thenReturn(currencyDTO);

        var result = scheduledTasks.updateCurrencyData();
        var currency = result.iterator().next();

        // then
        assertThat(result).hasSize(1);
        assertThat(currency.getBaseCurrency()).isEqualTo("GBP");
        assertThat(currency.getExchangeRates()).hasSize(1);
        verify(currencyService, times(1)).getAllCurrencies();
        verify(currencyService, times(1)).deleteCurrency(currency);
        verify(currencyService, times(1)).addCurrency(currencyDTO);
        verify(webClientService, times(1)).callExternalService(anyString());

    }

}