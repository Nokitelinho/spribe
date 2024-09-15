package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class WebClientServiceTest {

    @InjectMocks
    WebClientService webClientService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExchangeRateRequestUrl exchangeRateRequestUrl;

    private final String baseCurrency = "EUR";

    private CurrencyDTO populateCurrencyDTO() {
        var currencyDTO = new CurrencyDTO();
        currencyDTO.setSuccess(true);
        currencyDTO.setTimestamp(System.currentTimeMillis());
        currencyDTO.setBaseCurrency("GBP");
        currencyDTO.setDate(LocalDate.now());
        currencyDTO.setRates(Map.of("JPY", 1.856));
        return currencyDTO;
    }

    @Test
    void shouldReturnCurrency() {
        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);
        var currencyDTO = populateCurrencyDTO();

        // when
        when(restTemplate.getForEntity(requestUrl, CurrencyDTO.class))
                .thenReturn(new ResponseEntity<>(currencyDTO, HttpStatus.OK));

        var result = webClientService.fetchCurrencyData(requestUrl);

        // then
        assertThat(result).isEqualTo(currencyDTO);
        verify(restTemplate, times(1)).getForEntity(requestUrl, CurrencyDTO.class);
    }

    @Test
    public void testWebClientServiceThrowsRuntimeException() {

        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);

        // when
        when(restTemplate.getForEntity(requestUrl, CurrencyDTO.class))
                .thenThrow(new RuntimeException("RestTemplate failure"));

        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> webClientService.fetchCurrencyData(requestUrl))
                .withMessageContaining("An error occurred while fetching the data");
        verify(restTemplate, times(1)).getForEntity(requestUrl, CurrencyDTO.class);
    }

    @Test
    void testWebClientServiceThrowsRuntimeExceptionOnStatusCode() {

        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);
        var currencyDTO = populateCurrencyDTO();

        // when
        when(restTemplate.getForEntity(requestUrl, CurrencyDTO.class))
                .thenReturn(new ResponseEntity<>(currencyDTO, HttpStatus.BAD_REQUEST));

        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> webClientService.fetchCurrencyData(requestUrl))
                .withMessageContaining("An error occurred while fetching the data");
        verify(restTemplate, times(1)).getForEntity(requestUrl, CurrencyDTO.class);
    }

}