package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateHistoryResponse {

    private String countryName;
    private double rateValue;
    private LocalDateTime updatedAt;
}
