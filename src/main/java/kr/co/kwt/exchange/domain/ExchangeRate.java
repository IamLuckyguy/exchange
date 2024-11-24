package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Table("exchange_rates")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExchangeRate {

    @Id
    private Long id;
    private String country;
    private String countryFlag;
    private String countryCode;
    private String currencyCode;
    private Double rateValue;
    private LocalDateTime updatedAt;
//    private List<Rate> rateHistories;

    public static ExchangeRate withoutId(@NonNull final String country,
                                         @NonNull final String countryFlag,
                                         @NonNull final String countryCode,
                                         @NonNull final String currencyCode,
                                         @Nullable final Double rate) {
        return new ExchangeRate(null, country, countryFlag, countryCode, currencyCode, rate, LocalDateTime.now());
    }

    public static ExchangeRate withId(@NonNull final Long id,
                                      @NonNull final String country,
                                      @NonNull final String countryFlag,
                                      @NonNull final String countryCode,
                                      @NonNull final String currencyCode,
                                      @NonNull final double rateValue,
                                      @NonNull final LocalDateTime updatedAt) {
        return new ExchangeRate(id, country, countryFlag, countryCode, currencyCode, rateValue, updatedAt);
    }

    public void updateRate(final double rateValue) {
        Rate newRate = new Rate(rateValue, LocalDateTime.now());
    }
}
