package com.spribe.mappers;

import com.spribe.domain.Currency;
import com.spribe.domain.ExchangeRate;
import com.spribe.dto.CurrencyDTO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {

    @Mapping(source = "base", target = "baseCurrency")
    @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "rates", target = "rates", qualifiedByName = "dtoRatesToEntityRates")
    Currency dtoToEntity(CurrencyDTO currencyDTO);

    @Named("dtoRatesToEntityRates")
    default List<ExchangeRate>  dtoRatesToEntityRates(Map<String, Double> rates) {
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
