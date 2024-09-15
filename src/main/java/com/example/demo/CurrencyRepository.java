package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Query(value = "SELECT * FROM currency WHERE base_currency=:baseCurrencyCode", nativeQuery = true)
    Currency findExchangeRate(@Param("baseCurrencyCode") String baseCurrencyCode);
}
