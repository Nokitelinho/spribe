package com.example.demo.controller;

import com.example.demo.component.ExchangeRateRequestUrl;
import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.dto.CurrencyDTO;
import com.example.demo.service.CurrencyService;
import com.example.demo.service.WebClientService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    private final ExchangeRateRequestUrl exchangeRateRequestUrl;
    private final CurrencyService currencyService;
    private final WebClientService webClient;

    @GetMapping("/all")
    @Cacheable("currency")
    public Iterable<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/rate")
    @Cacheable("currency")
    public List<ExchangeRate> getExchangeRate(
            @RequestParam(name = "currencyCode") @NotBlank String currencyCode) {
        return currencyService.getExchangeRate(currencyCode);
    }

    @PostMapping("/add")
    @CacheEvict(value = "currency", allEntries = true)
    public Currency addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return currencyService.addCurrency(currencyDTO);
    }

    @GetMapping("/external")
    public CurrencyDTO getExternalData(
            @RequestParam(name = "currencyCode") @NotBlank String currencyCode
    ) {
        var requestUrl = exchangeRateRequestUrl.construct(currencyCode);

        return webClient.callExternalService(requestUrl);
    }
}
