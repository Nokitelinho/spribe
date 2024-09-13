package com.spribe.controller;

import com.spribe.domain.Currency;
import com.spribe.service.CurrencyService;
import com.spribe.dto.BaseCurrencyDTO;
import com.spribe.dto.CurrencyDTO;
import com.spribe.dto.ExchangeRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/all")
    @Cacheable("currencies")
    List<String> getBaseCurrenciesList() {
        return currencyService.getBaseCurrenciesList();
    }

    @GetMapping("/rates")
    @Cacheable("currencies")
    public List<ExchangeRateDTO> getExchangeRates(@RequestParam(name = "code") String baseCurrencyCode) {
        return currencyService.getExchangeRates(baseCurrencyCode);
    }

    @PostMapping("/save")
    @CacheEvict(value = "currencies", allEntries = true)
    public Currency saveCurrency(@RequestBody CurrencyDTO currencyDTO) {
        return currencyService.saveCurrencyData(currencyDTO);
    }

    @PostMapping("/add")
    @CacheEvict(value = "currencies", allEntries = true)
    public Currency addBaseCurrencyCode(@RequestBody BaseCurrencyDTO baseCurrencyDTO) {
        return currencyService.addBaseCurrencyCode(baseCurrencyDTO);
    }

}
