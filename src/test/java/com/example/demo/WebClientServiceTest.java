package com.example.demo;

import com.example.demo.component.ExchangeRateRequestUrl;
import com.example.demo.service.WebClientService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class WebClientServiceTest extends AbstractTest {

    @SpyBean
    private WebClientService webClientService;

    @MockBean
    private ExchangeRateRequestUrl exchangeRateRequestUrl;

    @MockBean
    private RestTemplate restTemplate;

    private final String baseCurrency = "EUR";

    @Test
    void shouldReturnCurrency() {
        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);
        var response = "JSON-STRING";

        // when
        when(restTemplate.getForEntity(requestUrl, String.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        var result = webClientService.callExternalService(requestUrl);

        // then
        assertThat(result).isEqualTo(response);
        verify(restTemplate, atLeast(1)).getForEntity(requestUrl, String.class);
    }

    @Test
    public void testWebClientServiceThrowsRuntimeException() {

        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);

        // when
        when(restTemplate.getForEntity(requestUrl, String.class))
                .thenThrow(new RuntimeException("RestTemplate failure"));

        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> webClientService.callExternalService(requestUrl))
                .withMessageContaining("RestTemplate failure");
        verify(restTemplate, atLeast(2)).getForEntity(requestUrl, String.class);
    }

    @Test
    void testWebClientServiceThrowsRuntimeExceptionOnStatusCode() {

        // given
        var requestUrl = exchangeRateRequestUrl.construct(baseCurrency);
        var response = "JSON-STRING";

        // when
        when(restTemplate.getForEntity(requestUrl, String.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));

        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> webClientService.callExternalService(requestUrl))
                .withMessageContaining("Failed to fetch data, status code: 400 BAD_REQUEST");
        verify(restTemplate, atLeast(3)).getForEntity(requestUrl, String.class);
    }

}