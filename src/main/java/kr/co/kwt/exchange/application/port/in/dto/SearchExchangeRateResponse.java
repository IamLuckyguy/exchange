package kr.co.kwt.exchange.application.port.in.dto;

import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class SearchExchangeRateResponse {

    private String country;
    private String countryFlag;
    private String countryCode;
    private String currencyCode;
    private double rateValue;
    private LocalDateTime updatedAt;

    public static SearchExchangeRateResponse of(final ExchangeRate exchangeRate) {
        return new SearchExchangeRateResponse(exchangeRate.getCountry(),
                exchangeRate.getCountryFlag(),
                exchangeRate.getCountryCode(),
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getUpdatedAt());
    }
}
