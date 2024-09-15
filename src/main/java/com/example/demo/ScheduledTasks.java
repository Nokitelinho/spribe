package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    @Value("${vendor.api.key}")
    private String apiKey;

    @Value("${vendor.base.url}")
    private String baseUrl;

    private final CurrencyService currencyService;
    private final WebClientService webClientService;
    private final ExchangeRateRequestUrl exchangeRateRequestUrl;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public Iterable<Currency> updateCurrencyData() {
        log.info("Entering ScheduledTasks - {}", "getCurrencyData");

        Iterable<Currency> currencyList =  currencyService.getAllCurrencies();

        currencyList.forEach(currency -> {
            var requestUrl = exchangeRateRequestUrl.construct(currency.getBaseCurrency());
            var currencyDTO = webClientService.fetchCurrencyData(requestUrl);

            if (Objects.nonNull(currencyDTO)) {
                currencyService.deleteCurrency(currency);
                currencyService.addCurrency(currencyDTO);
            }
        });

        log.info("Exiting ScheduledTasks - {}", "getCurrencyData");

        return currencyList;
    }
}
