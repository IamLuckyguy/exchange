package kr.co.kwt.exchange.application.port.dto;

import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeByRoundResult {

    private String countryName;
    private String countryFlag;
    private String currencyCode;
    private List<GetRoundRateResult> dailyRoundRates;

    public GetExchangeByRoundResult(final String currencyCode, final List<GetRoundRateResult> dailyRoundRates) {
        Country country = Country.of(currencyCode);
        this.currencyCode = currencyCode;
        this.countryName = country.getCountryName();
        this.countryFlag = country.getCountryFlag();
        this.dailyRoundRates = dailyRoundRates;
    }

    public static GetExchangeByRoundResult of(final String currencyCode, final List<RoundRate> roundRates) {
        Country country = Country.of(currencyCode);
        return new GetExchangeByRoundResult(
                country.getCountryName(),
                country.getCountryFlag(),
                country.getCurrencyCode(),
                roundRates.stream().map(GetRoundRateResult::of).toList()
        );
    }
}
