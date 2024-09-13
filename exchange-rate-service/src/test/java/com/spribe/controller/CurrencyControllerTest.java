package com.spribe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spribe.domain.Currency;
import com.spribe.dto.BaseCurrencyDTO;
import com.spribe.dto.CurrencyDTO;
import com.spribe.dto.ExchangeRateDTO;
import com.spribe.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBaseCurrenciesList() throws Exception {
        List<String> baseCurrencies = Collections.singletonList("USD");

        when(currencyService.getBaseCurrenciesList()).thenReturn(baseCurrencies);

        mockMvc.perform(get("/api/currency/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(baseCurrencies)));
    }

    @Test
    public void testGetExchangeRates() throws Exception {
        List<ExchangeRateDTO> exchangeRateDTOs = Collections.singletonList(new ExchangeRateDTO());

        when(currencyService.getExchangeRates("USD")).thenReturn(exchangeRateDTOs);

        mockMvc.perform(get("/api/currency/rates")
                        .param("code", "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(exchangeRateDTOs)));
    }

    @Test
    public void testSaveCurrency() throws Exception {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        Currency currency = new Currency();

        when(currencyService.saveCurrencyData(currencyDTO)).thenReturn(currency);

        mockMvc.perform(post("/api/currency/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddBaseCurrencyCode() throws Exception {
        BaseCurrencyDTO baseCurrencyDTO = new BaseCurrencyDTO();
        Currency currency = new Currency();

        when(currencyService.addBaseCurrencyCode(baseCurrencyDTO)).thenReturn(currency);

        mockMvc.perform(post("/api/currency/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseCurrencyDTO)))
                .andExpect(status().isOk());
    }
}