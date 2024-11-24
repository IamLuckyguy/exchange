package kr.co.kwt.exchange.application.port.in.dto;

import lombok.Getter;

@Getter
public class AddExchangeRateRequest {

    private String country;
    private String countryCode;
    private String countryFlag;
    private String currencyCode;
}
