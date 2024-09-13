package com.spribe.scheduler;

import com.spribe.dto.CurrencyDTO;
import com.spribe.service.CurrencyService;
import com.spribe.service.WebClientService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
@EnableScheduling
public class ScheduledTasksTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private WebClientService webClientService;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    public ScheduledTasksTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrencyData() {
        // Mocking the behavior of currencyService and webClientService
        when(currencyService.getBaseCurrenciesList()).thenReturn(Collections.singletonList("USD"));
        when(webClientService.fetchData(anyString())).thenReturn(Mono.just(new CurrencyDTO()));

        // Call the method
        scheduledTasks.getCurrencyData();

        // Verify interactions with the services
        verify(currencyService, times(0)).getBaseCurrenciesList();
        verify(webClientService, times(0)).fetchData("USD");
        verify(currencyService, times(0)).deleteByBaseCurrencyCode("USD");
        verify(currencyService, times(0)).saveCurrencyData(any(CurrencyDTO.class));
    }
}