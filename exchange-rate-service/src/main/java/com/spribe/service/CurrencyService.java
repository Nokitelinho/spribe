package com.spribe.service;

import com.spribe.domain.Currency;
import com.spribe.dto.BaseCurrencyDTO;
import com.spribe.dto.CurrencyDTO;
import com.spribe.dto.ExchangeRateDTO;
import com.spribe.mappers.CurrencyMapper;
import com.spribe.mappers.ExchangeRateMapper;
import com.spribe.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final WebClientService webClientService;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateMapper exchangeRateMapper;
    private final CurrencyMapper currencyMapper;

    public Currency saveCurrencyData(CurrencyDTO currencyDTO) {
        Currency currency = currencyMapper.dtoToEntity(currencyDTO);

        return currencyRepository.save(currency);
    }

    public List<String> getBaseCurrenciesList() {
        return currencyRepository.findBaseCurrencies();
    }

    public List<ExchangeRateDTO> getExchangeRates(String baseCurrencyCode) {
        var currency = currencyRepository.findByBaseCurrencyCode(baseCurrencyCode);

        if (Objects.nonNull(currency)) {
            return exchangeRateMapper.toDto(currency.getRates());
        }

        return new ArrayList<>();
    }

    public Currency addBaseCurrencyCode(BaseCurrencyDTO baseCurrencyDTO) {
        var baseCurrencyCode = baseCurrencyDTO.getBaseCurrency().toUpperCase();
        var currency = currencyRepository.findByBaseCurrencyCode(baseCurrencyCode);

        if (Objects.nonNull(currency)) {
            currencyRepository.delete(currency);
        }
        var currencyDTO = webClientService.fetchData(baseCurrencyCode).block();

        if (Objects.nonNull(currencyDTO)) {
            return saveCurrencyData(currencyDTO);
        }

        return currency;
    }

    public void deleteByBaseCurrencyCode(String baseCurrencyCode) {
        var currency = currencyRepository.findByBaseCurrencyCode(baseCurrencyCode);
        if (Objects.nonNull(currency)) {
            currencyRepository.delete(currency);
        }
    }
}
