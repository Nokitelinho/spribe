package com.spribe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyDTO {

    private boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;

    // Getters and Setters
}
