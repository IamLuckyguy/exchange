package kr.co.kwt.exchange.application.port.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.domain.ClosingRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetClosingRateResult {

    @JsonProperty("rv")
    private double closingRate;
    @JsonProperty("at")
    private LocalDateTime fetchedAt;

    public static GetClosingRateResult of(final ClosingRate closingRate) {
        return new GetClosingRateResult(
                closingRate.getClosingRate(),
                closingRate.getFetchedAt()
        );
    }
}
