package com.example.demo.service;

import com.example.demo.component.ScheduledTasks;
import com.example.demo.dto.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebClientService {
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

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
            throw new RuntimeException("An error occurred while fetching the data", e);
        }

    }
}
