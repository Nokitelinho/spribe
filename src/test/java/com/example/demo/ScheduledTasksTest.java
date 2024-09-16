package com.example.demo;

import com.example.demo.component.ScheduledTasks;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.service.WebClientService;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledTasksTest {

    @SpyBean
    ScheduledTasks tasks;

    @MockBean
    WebClientService webClientService;

    private CurrencyDTO populateCurrencyDTO() {
        var currencyDTO = new CurrencyDTO();
        currencyDTO.setSuccess(true);
        currencyDTO.setTimestamp(System.currentTimeMillis());
        currencyDTO.setBaseCurrency("GBP-TEST");
        currencyDTO.setDate(LocalDate.now());
        currencyDTO.setRates(Map.of("JPY", 1.856));

        return currencyDTO;
    }

    @Test
    public void verifyThatTaskTriggered() {
        // given
        var currencyDTO = populateCurrencyDTO();

        // when
        when(webClientService.callExternalService(anyString())).thenReturn(currencyDTO);

        // then
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            verify(tasks, atLeast(2)).updateCurrencyData();
        });
    }

}