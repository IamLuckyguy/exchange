package kr.co.kwt.exchange.application.port.dto;

import kr.co.kwt.exchange.domain.RoundRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRoundRateResult {

    private double roundRate;
    private int round;
    private String trend;
    private double trendRate;
    private String liveStatus;
    private String marketStatus;
    private LocalDateTime fetchedAt;

    public static GetRoundRateResult of(final RoundRate roundRate) {
        return new GetRoundRateResult(
                roundRate.getRoundRate(),
                roundRate.getRound(),
                roundRate.getTrend(),
                roundRate.getTrendRate(),
                roundRate.getLiveStatus(),
                roundRate.getMarketStatus(),
                roundRate.getFetchedAt()
        );
    }
}
