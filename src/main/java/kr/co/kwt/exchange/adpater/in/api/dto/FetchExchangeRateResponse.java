package kr.co.kwt.exchange.adpater.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchExchangeRateResponse {

    private Long id;
    private String currencyCode;
    private Double rateValue;
    private LocalDateTime updateTime;
}
