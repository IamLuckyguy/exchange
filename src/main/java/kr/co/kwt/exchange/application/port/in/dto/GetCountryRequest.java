package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCountryRequest {

    private final String currencyCode;
}
