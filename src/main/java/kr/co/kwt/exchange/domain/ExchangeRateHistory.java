package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Table("exchange_rate_histories")
@Getter
@AllArgsConstructor
public class ExchangeRateHistory {

    private final String currencyCode;
    private final double rateValue;
    private final LocalDateTime updatedAt;

    public static ExchangeRateHistory of(@NonNull final ExchangeRate exchangeRate) {
        return new ExchangeRateHistory(
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getUpdatedAt()
        );
    }
}
