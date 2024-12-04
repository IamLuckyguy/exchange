package kr.co.kwt.exchange.application.port.in.dto;

import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchExchangeRateWithCountryResponse {

    private String country;
    private String countryFlag;
    private String currencyCode;
    private double rateValue;
    private LocalDateTime updatedAt;

    public static SearchExchangeRateWithCountryResponse of(final ExchangeRate exchangeRate, final Country country) {
        return new SearchExchangeRateWithCountryResponse(
                country.getCountryName(),
                country.getCountryFlag(),
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getUpdatedAt());
    }
}
