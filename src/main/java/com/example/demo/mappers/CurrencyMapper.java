package com.example.demo.mappers;

import com.example.demo.domain.Currency;
import com.example.demo.domain.ExchangeRate;
import com.example.demo.dto.CurrencyDTO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {

    @Mapping(source = "rates", target = "exchangeRates", qualifiedByName = "dtoRatesToEntityRates")
    Currency dtoToEntity(CurrencyDTO currencyDTO);

    @Named("dtoRatesToEntityRates")
    default List<ExchangeRate> dtoRatesToEntityRates(Map<String, Double> rates) {
        List<ExchangeRate> rateList = new ArrayList<>();

        rates.forEach((currencyCode, rateValue) -> {
            ExchangeRate rate = new ExchangeRate();
            rate.setCurrencyCode(currencyCode);
            rate.setRate(rateValue);
            rateList.add(rate);
        });

        return rateList;
    }
}
