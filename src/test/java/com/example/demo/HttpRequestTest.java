package com.example.demo;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.mappers.CurrencyMapper;
import com.example.demo.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @SpyBean
    CurrencyMapper currencyMapper;

    @Autowired
    private TestRestTemplate restTemplate;

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
    void shouldAddCurrency() throws Exception {

        // given
        var baseUrl = "/api/v1/currency/add";

        var currency = new CurrencyDTO();
        currency.setSuccess(true);
        currency.setTimestamp(System.currentTimeMillis());
        currency.setBaseCurrency("GBP");
        currency.setDate(LocalDate.now());
        currency.setRates(Map.of("JPY", 1.856));

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CurrencyDTO> request = new HttpEntity<>(currency, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"baseCurrency\":\"GBP\"");
    }

    @Test
    void shouldReturnListCurrencies() throws Exception {

        // given
        var baseUrl = "/api/v1/currency/all";

        // when
        currencyRepository.save(populateCurrency());

        // then
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/" + baseUrl,
                String.class)).contains("\"baseCurrency\":\"GBP\"");
    }

    @Test
    void shouldReturnExchangeRate() throws Exception {

        // given
        var baseUrl = "/api/v1/currency/rate?currencyCode=GBP";

        // when
        currencyRepository.save(populateCurrency());

        // then
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/" + baseUrl,
                String.class)).contains("\"currencyCode\":\"JPY\",\"rate\":1.856");
    }
}
