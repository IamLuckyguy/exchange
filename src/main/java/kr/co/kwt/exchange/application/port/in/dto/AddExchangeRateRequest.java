package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddExchangeRateRequest {

    private String currencyCode;
    private Double rateValue;
    private LocalDateTime fetchedAt;
}
