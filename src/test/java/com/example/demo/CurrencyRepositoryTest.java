package com.example.demo;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp () {
        currencyRepository.deleteAll();
    }

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
    void shouldSaveCurrency() {
        // given
        var currency = populateCurrency();

        // when
        var result = currencyRepository.save(currency);

        // then
        assertThat(currencyRepository.findById(result.getId()).get()).isEqualTo(result);

    }

    @Test
    void shouldFindExchangeRate() {
        // given
        var currency = populateCurrency();

        // when
        var savedCurrency = currencyRepository.save(currency);
        var result = currencyRepository.findById(savedCurrency.getId()).orElse(new Currency());
        var exchangeRate = result.getExchangeRates().stream().findFirst().get();

        // then
        assertThat(result.getExchangeRates()).hasSize(1);
        assertThat(exchangeRate.getRate()).isEqualTo(1.856);
        assertThat(exchangeRate.getCurrencyCode()).isEqualTo("JPY");
    }

}