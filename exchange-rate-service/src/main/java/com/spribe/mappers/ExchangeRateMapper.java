package com.spribe.mappers;

import com.spribe.domain.ExchangeRate;
import com.spribe.dto.ExchangeRateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRateMapper {

    List<ExchangeRateDTO> toDto(List<ExchangeRate> exchangeRate);
}
