package kr.co.kwt.exchange.application.port.in.dto;

import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateResponse {

    private String countryName;
    private String countryFlag;
    private String currencyCode;

    @Setter
    private List<GetExchangeRateHistoryResponse> exchangeRateHistories;
    private Integer unitAmount;
    private Integer decimals;
    private LocalDateTime fetchedAt;
    private LocalDateTime updatedAt;

    public static GetExchangeRateResponse of(final ExchangeRate exchangeRate,
                                             final Country country,
                                             final List<GetExchangeRateHistoryResponse> getExchangeRateHistories) {
        return new GetExchangeRateResponse(
                country.getCountryName(),
                country.getCountryFlag(),
                exchangeRate.getCurrencyCode(),
                getExchangeRateHistories,
                exchangeRate.getUnitAmount(),
                exchangeRate.getDecimals(),
                exchangeRate.getFetchedAt(),
                exchangeRate.getUpdatedAt());
    }
}
