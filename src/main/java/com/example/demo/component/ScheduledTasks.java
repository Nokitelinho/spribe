package com.example.demo.component;

import com.example.demo.domain.Currency;
import com.example.demo.service.CurrencyService;
import com.example.demo.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
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

    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void updateCurrencyData() {
        log.info("Entering ScheduledTasks - {}", "getCurrencyData");

        Iterable<Currency> currencyList =  currencyService.getAllCurrencies();

        currencyList.forEach(currency -> {
            var jsonData = getCurrencyData(currency);

            if (Objects.nonNull(jsonData)) {
                currencyService.deleteCurrency(currency);
                currencyService.addCurrency(jsonData);
            }
        });

        log.info("Exiting ScheduledTasks - {}", "getCurrencyData");

    }

    private String getCurrencyData(Currency currency) {
        var requestUrl = exchangeRateRequestUrl.construct(currency.getBaseCurrency());

        return webClientService.callExternalService(requestUrl);
    }
}
