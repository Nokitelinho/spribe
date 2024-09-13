package com.spribe.repository;

import com.spribe.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query(value = "SELECT base_currency FROM currency", nativeQuery = true)
    List<String> findBaseCurrencies();

    @Query(value = "SELECT * FROM currency WHERE base_currency=:baseCurrencyCode", nativeQuery = true)
    Currency findByBaseCurrencyCode(@Param("baseCurrencyCode") String baseCurrencyCode);

    @Query(value = "SELECT base_currency FROM currency WHERE base_currency=:baseCurrencyCode LIMIT 1", nativeQuery = true)
    String findBaseCurrencyCode(String baseCurrencyCode);
}
