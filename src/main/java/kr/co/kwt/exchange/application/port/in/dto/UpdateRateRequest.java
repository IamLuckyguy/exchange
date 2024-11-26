package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRateRequest {

    private String country;
    private String currencyCode;
    private double rateValue;
}
