package com.example.demo;

import com.example.demo.component.ExchangeRateRequestUrl;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.service.WebClientService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

    @SpyBean
    private WebClientService webClientService;

    @MockBean
    private ExchangeRateRequestUrl exchangeRateRequestUrl;

    @MockBean
    private RestTemplate restTemplate;

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

        var result = webClientService.callExternalService(requestUrl);

        // then
        assertThat(result).isEqualTo(currencyDTO);
        verify(restTemplate, atLeast(1)).getForEntity(requestUrl, CurrencyDTO.class);
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
                .isThrownBy(() -> webClientService.callExternalService(requestUrl))
                .withMessageContaining("RestTemplate failure");
        verify(restTemplate, atLeast(2)).getForEntity(requestUrl, CurrencyDTO.class);
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
                .isThrownBy(() -> webClientService.callExternalService(requestUrl))
                .withMessageContaining("Failed to fetch data, status code: 400 BAD_REQUEST");
        verify(restTemplate, atLeast(3)).getForEntity(requestUrl, CurrencyDTO.class);
    }

}