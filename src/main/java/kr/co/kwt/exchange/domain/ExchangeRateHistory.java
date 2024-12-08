package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Table("exchange_rate_histories")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateHistory {

    @Id
    private Long id;
    private String currencyCode;
    private double rateValue;
    private LocalDateTime fetchedAt;
    private LocalDateTime updatedAt;

    public static ExchangeRateHistory of(@NonNull final ExchangeRate exchangeRate) {
        return new ExchangeRateHistory(
                null,
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getFetchedAt(),
                exchangeRate.getUpdatedAt()
        );
    }
}
