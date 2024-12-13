package kr.co.kwt.exchange.adapter.in.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FetchExchangeRateRequest {

    private LocalDateTime fetchDateTime = LocalDateTime.now();
}
