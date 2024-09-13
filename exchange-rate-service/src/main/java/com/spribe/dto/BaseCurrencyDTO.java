package com.spribe.dto;

import com.spribe.domain.ExchangeRate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseCurrencyDTO {

    private String baseCurrency;
    private List<ExchangeRate> rates;

    // Getters and Setters
}

