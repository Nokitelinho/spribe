package com.example.demo;

import com.example.demo.domain.Currency;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.mappers.CurrencyMapper;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyServiceTest {

    @MockBean
    CurrencyRepository currencyRepository;

    @MockBean
    CurrencyMapper currencyMapper;

    @SpyBean
    CurrencyService currencyService;

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
        var currencyDTO = new CurrencyDTO();
        var currency = new Currency();

        // when
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);
        when(currencyMapper.dtoToEntity(any(CurrencyDTO.class))).thenReturn(currency);

        var result = currencyService.addCurrency(currencyDTO);

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