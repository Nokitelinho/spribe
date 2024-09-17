package com.example.demo.service.impl;

import com.example.demo.dto.CurrencyDTO;
import com.example.demo.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientServiceImpl implements WebClientService {
    private final RestTemplate restTemplate;

    @Override
    @Retryable(
            maxAttempts = 3, // Retry up to 3 times
            backoff = @Backoff(delay = 2000) // Wait for 2 seconds between retries
    )
    public CurrencyDTO callExternalService(String requestUrl) {

        log.info("Entering WebClientService fetchCurrencyData - {}", requestUrl);

        try {
            ResponseEntity<CurrencyDTO> response = restTemplate.getForEntity(requestUrl, CurrencyDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                // Handle non-200 status codes
                throw new RuntimeException("Failed to fetch data, status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
