package kr.co.kwt.exchange.application.port.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchExchangeResult {

    private Long roundRateFetchedCount;
    private Long closingRateFetchedCount;
    private Integer round;
}
