package kr.co.kwt.exchange.adapter.in.web.event;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetRoundRateResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FetchEventResponse {

    private List<GetExchangeByRoundResult> exchangeRateRealTime;
    private LocalDateTime updatedAt;

    public FetchEventResponse(List<GetExchangeByRoundResult> getExchangeByRoundResults) {
        exchangeRateRealTime = getExchangeByRoundResults;
        updatedAt = getUpdatedAtFromExchangeRate(getExchangeByRoundResults);
    }

    private LocalDateTime getUpdatedAtFromExchangeRate(List<GetExchangeByRoundResult> exchangeRates) {
        return exchangeRates.stream()
                .findFirst()
                .flatMap(result -> result.
                        getDailyRoundRates().
                        stream().
                        findFirst())
                .map(GetRoundRateResult::getFetchedAt)
                .orElseGet(LocalDateTime::now);
    }
}
