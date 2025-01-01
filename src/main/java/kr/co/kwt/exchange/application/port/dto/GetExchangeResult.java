package kr.co.kwt.exchange.application.port.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeResult {

    private String countryName;
    private String countryFlag;
    private String currencyCode;
    @JsonProperty("exchangeRateRealTime")
    private List<GetRoundRateResult> dailyRoundRates;
    @JsonProperty("exchangeRateHistories")
    private List<GetClosingRateResult> yearlyClosingRates;
    private Integer decimals;
    private Integer unit;

    public static GetExchangeResult of(final Exchange exchange) {
        return new GetExchangeResult(
                exchange.getCountry().getCountryName(),
                exchange.getCountry().getCountryFlag(),
                exchange.getCountry().getCurrencyCode(),
                exchange.getDailyRoundRates().stream().map(GetRoundRateResult::of).toList(),
                exchange.getYearlyClosingRates().stream().map(GetClosingRateResult::of).toList(),
                exchange.getDecimals(),
                exchange.getUnit()
        );
    }
}
