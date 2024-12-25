package kr.co.kwt.exchange.application.port.dto;

import lombok.Getter;

@Getter
public class UpdateExchangeCommand {

    private String currencyCode;
    private Integer unit;
    private Integer decimals;
}
