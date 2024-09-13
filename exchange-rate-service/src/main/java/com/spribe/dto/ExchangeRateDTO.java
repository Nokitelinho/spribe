package com.spribe.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRateDTO {
    private Long id;
    private String currencyCode;
    private Double rate;

    // Getters and Setters
}