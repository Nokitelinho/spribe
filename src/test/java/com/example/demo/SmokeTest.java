package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private CurrencyController currencyController;

    @Test
    void contextLoads() throws Exception {
        assertThat(currencyController).isNotNull();
    }
}
