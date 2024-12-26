package kr.co.kwt.exchange.application.port.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRoundRateResult {

    @JsonProperty("rv")
    private double roundRate;
    @JsonProperty("r")
    private int round;
    @JsonProperty("t")
    private String trend;
    @JsonProperty("tr")
    private double trendRate;
    @JsonProperty("live")
    private String liveStatus;
    @JsonProperty("market")
    private String marketStatus;
    @JsonProperty("at")
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
