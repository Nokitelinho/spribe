package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {
    private Long id;
    private boolean success;
    private Long timestamp;

    @JsonProperty("base")
    private String baseCurrency;
    private LocalDate date;
    private Map<String, Double> rates;
}
