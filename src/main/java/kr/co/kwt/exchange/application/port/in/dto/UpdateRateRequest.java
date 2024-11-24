package kr.co.kwt.exchange.application.port.in.dto;

import lombok.Getter;

@Getter
public class UpdateRateRequest {

    private String country;
    private double rateValue;
}
