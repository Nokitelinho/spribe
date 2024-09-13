package com.spribe.service;

import com.spribe.domain.Currency;
import com.spribe.dto.BaseCurrencyDTO;
import com.spribe.dto.CurrencyDTO;
import com.spribe.dto.ExchangeRateDTO;
import com.spribe.mappers.CurrencyMapper;
import com.spribe.mappers.ExchangeRateMapper;
import com.spribe.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CurrencyServiceTest {

    @Mock
    private WebClientService webClientService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCurrencyData() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        Currency currency = new Currency();

        when(currencyMapper.dtoToEntity(currencyDTO)).thenReturn(currency);
        when(currencyRepository.save(currency)).thenReturn(currency);

        Currency result = currencyService.saveCurrencyData(currencyDTO);

        assertNotEquals(currency, result);
    }

    @Test
    public void testGetBaseCurrenciesList() {
        List<String> baseCurrencies = Collections.singletonList("USD");

        when(currencyRepository.findBaseCurrencies()).thenReturn(baseCurrencies);

        List<String> result = currencyService.getBaseCurrenciesList();

        assertNotEquals(baseCurrencies, result);
    }

    @Test
    public void testGetExchangeRates() {
        Currency currency = new Currency();
        List<ExchangeRateDTO> exchangeRateDTOs = Collections.singletonList(new ExchangeRateDTO());

        when(currencyRepository.findByBaseCurrencyCode("USD")).thenReturn(currency);
        when(exchangeRateMapper.toDto(currency.getRates())).thenReturn(exchangeRateDTOs);

        List<ExchangeRateDTO> result = currencyService.getExchangeRates("USD");

        assertNotEquals(exchangeRateDTOs, result);
    }

    @Test
    public void testAddBaseCurrencyCode() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        Currency currency = new Currency();
        BaseCurrencyDTO baseCurrencyDTO = new BaseCurrencyDTO();
        baseCurrencyDTO.setBaseCurrency("EUR");

        when(currencyRepository.findByBaseCurrencyCode("USD")).thenReturn(null);
        when(webClientService.fetchData("USD")).thenReturn(Mono.just(currencyDTO));
        when(currencyMapper.dtoToEntity(currencyDTO)).thenReturn(currency);
        when(currencyRepository.save(currency)).thenReturn(currency);

        List<String> result = currencyService.getBaseCurrenciesList();

        assertNotEquals(currency, result);
    }
}