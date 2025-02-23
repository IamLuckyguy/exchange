package kr.co.kwt.exchange.application.port.dto;

import kr.co.kwt.exchange.domain.Exchange;
import kr.co.kwt.exchange.domain.Round;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchExchangeCommand {

    private Integer round;
    private LocalDateTime fetchedAt;
    private List<FetchRate> fetchRates;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FetchRate {
        private String currencyCode;
        private Double roundRate;
        private String trend;
        private Double trendRate;
        private Double trendDiff;
        private String marketStatus;
        private String liveStatus;
        private LocalDateTime fetchedAt;

        public RoundRate toRoundRate(Exchange exchange, Round round) {
            return RoundRate.withoutId(
                    exchange,
                    round,
                    round.getRound(),
                    roundRate,
                    trend,
                    trendRate,
                    trendDiff,
                    liveStatus,
                    marketStatus,
                    fetchedAt
            );
        }
    }
}
