package kr.co.kwt.exchange.application.port.in.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchExchangeRateWithCountryRequest {

    private String currencyCode;
}
