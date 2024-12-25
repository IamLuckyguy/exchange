package kr.co.kwt.exchange.application.port.dto;

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
        private String marketStatus;
        private String liveStatus;
    }
}
