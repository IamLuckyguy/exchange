package kr.co.kwt.exchange.application.port.dto;

import kr.co.kwt.exchange.domain.Country;
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

    public GetExchangeByRoundResult(final String currencyCode, final Country country, final List<GetRoundRateResult> dailyRoundRates) {
        this.currencyCode = currencyCode;
        this.countryName = country.getCountryName();
        this.countryFlag = country.getCountryFlag();
        this.dailyRoundRates = dailyRoundRates;
    }
}
