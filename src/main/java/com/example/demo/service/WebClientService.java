package com.example.demo.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public interface WebClientService {
    @Retryable(
            maxAttempts = 3, // Retry up to 3 times
            backoff = @Backoff(delay = 2000) // Wait for 2 seconds between retries
    )
    String callExternalService(String requestUrl);
}
