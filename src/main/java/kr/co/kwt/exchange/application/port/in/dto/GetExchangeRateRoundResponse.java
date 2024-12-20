package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateRoundResponse {

    private int degreeCount;
    private LocalDateTime fetchedAt;
}
