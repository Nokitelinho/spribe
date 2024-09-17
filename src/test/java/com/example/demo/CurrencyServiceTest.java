package com.example.demo;

import com.example.demo.domain.Currency;
import com.example.demo.provider.CurrencyRateProvider;
import com.example.demo.provider.ProviderExchangeratesapi;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CurrencyServiceTest extends AbstractTest {

    @MockBean
    CurrencyRepository currencyRepository;

    @MockBean
    ProviderExchangeratesapi providerExchangeratesapi;

    @SpyBean
    CurrencyService currencyService;

    @Value("${provider.name}")
    private String providerName;

    @Autowired
    Map<String, CurrencyRateProvider> providers;

    @Test
    public void shouldReturnCurrencyList() {
        // given
        List<Currency> currencyList = List.of(new Currency());

        // when
        when(currencyRepository.findAll()).thenReturn(currencyList);

        var result = currencyService.getAllCurrencies();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void shouldAddCurrency() {
        // given
        var currency = new Currency();

        CurrencyRateProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new RuntimeException("Provider not found: " + providerName);
        }

        // when
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);
        when(provider.getCurrency(anyString())).thenReturn(currency);

        var result = currencyService.addCurrency("{ \"name\": \"John\", \"age\": 30 }");

        // then
        assertThat(result).isEqualTo(currency);
    }

    @Test
    public void shouldReturnExchangeRate() {
        // given
        var currency = new Currency();

        // when
        when(currencyRepository.findExchangeRate(anyString())).thenReturn(currency);

        var result = currencyService.getExchangeRate("EUR");

        // then
        assertThat(result).isEqualTo(currency.getExchangeRates());
    }

}