package kr.co.kwt.exchange.application.port.in.dto;

import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRateValueResponse {

    private String currencyCode;
    private double rateValue;
    private LocalDateTime updateTime;

    public static UpdateRateValueResponse of(ExchangeRate exchangeRate) {
        return new UpdateRateValueResponse(
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getUpdatedAt());
    }

}
