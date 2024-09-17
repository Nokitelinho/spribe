package com.example.demo;

import com.example.demo.controller.CurrencyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SmokeTest extends AbstractTest {

    @Autowired
    private CurrencyController currencyController;

    @Test
    void contextLoads() throws Exception {
        assertThat(currencyController).isNotNull();
    }
}
