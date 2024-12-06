package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExchangeRateHistory {

    private final String currencyCode;
    private final double rateValue;
    private final Integer decimals;
    private final LocalDateTime updatedAt;
}
