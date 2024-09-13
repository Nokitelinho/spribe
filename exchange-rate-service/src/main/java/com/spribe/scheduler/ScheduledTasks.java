package com.spribe.scheduler;

import com.spribe.service.CurrencyService;
import com.spribe.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final CurrencyService currencyService;
    private final WebClientService webClientService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void getCurrencyData() {
        log.info("Entering ScheduledTasks - {}", "getCurrencyData");

        List<String> baseCurrenciesList =  currencyService.getBaseCurrenciesList();

        baseCurrenciesList.forEach(currencyCode -> {
            var currencyDTO = webClientService.fetchData(currencyCode).block();

            if (Objects.nonNull(currencyDTO)) {
                currencyService.deleteByBaseCurrencyCode(currencyCode);
                currencyService.saveCurrencyData(currencyDTO);
            }
        });

        log.info("Exiting ScheduledTasks - {}", "getCurrencyData");
    }
}