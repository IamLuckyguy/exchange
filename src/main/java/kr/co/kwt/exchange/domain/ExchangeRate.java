package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Table("exchange_rates")
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExchangeRate {

    @Id
    private Long id;
    private String currencyCode;
    private Integer unitAmount;
    private Double rateValue;
    private LocalDateTime updatedAt;

    public static ExchangeRate withoutId(@NonNull final String currencyCode,
                                         @Nullable final Integer unitAmount,
                                         @Nullable final Double rate) {
        return new ExchangeRate(null, currencyCode, unitAmount, rate, LocalDateTime.now());
    }

    public static ExchangeRate withId(@NonNull final Long id,
                                      @NonNull final String currencyCode,
                                      @Nullable final Integer unitAmount,
                                      @NonNull final double rateValue,
                                      @NonNull final LocalDateTime updatedAt) {
        return new ExchangeRate(id, currencyCode, unitAmount, rateValue, updatedAt);
    }

    public ExchangeRate updateRate(final double newRateValue) {
        rateValue = newRateValue;
        updatedAt = LocalDateTime.now();
        return this;
    }
}
