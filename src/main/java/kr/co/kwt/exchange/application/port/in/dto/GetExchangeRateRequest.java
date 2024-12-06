package kr.co.kwt.exchange.application.port.in.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetExchangeRateRequest {

    private String currencyCode;
}
