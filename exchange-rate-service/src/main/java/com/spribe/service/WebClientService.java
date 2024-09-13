package com.spribe.service;

import com.spribe.dto.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    private static final String API_KEY = "27c0bc55dd86fdc518e198aaee99723f";
    private static final Logger log = LoggerFactory.getLogger(WebClientService.class);

    public Mono<CurrencyDTO> fetchData(String baseCurrencyCode) {
        log.info("Entering ExternalApiService - {}", "getDataFromExternalApi");

        return webClient.get()
                .uri("/v1/latest?access_key=" + API_KEY + "&base=" + baseCurrencyCode)
                .retrieve()
                .bodyToMono(CurrencyDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(5))
                        .filter(throwable -> throwable instanceof RuntimeException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()))
                .onErrorResume(ex -> Mono.error(new RuntimeException("Error in WebClientService", ex)));
    }
}
