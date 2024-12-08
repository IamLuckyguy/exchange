package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

/**
 * <h1>환율 도메인</h1>
 * <p>1. 환율 코드는 유일해야 한다.</p>
 * <p>2. 한화, 엔화를 제외한 환율은 소수점 2자리까지 유지한다.</p>
 * <p>3. 환율 단위량, 해당 통화의 금액에 대한 기본 단위량을 의미하고, 해당 단위량은 통화 코드에 괄호 안에 포함된다. ex) JPY(100)</p>
 * <p>4. 베이스 환율은 한화로 설정한다.</p>
 */

@Table("exchange_rates")
@Getter
@ToString
@AllArgsConstructor(access = PRIVATE)
public final class ExchangeRate {

    @Id
    private Long id;
    private String currencyCode;

    @Transient
    private Country country;
    private Integer unitAmount;
    private Integer decimals;
    private Double rateValue;
    private LocalDateTime fetchedAt;
    private LocalDateTime updatedAt;

    public static ExchangeRate withoutId(@Nullable final Country country,
                                         @NonNull final String currencyCode,
                                         @Nullable final Double rate,
                                         @NonNull final LocalDateTime fetchedAt
    ) {
        return new ExchangeRate(null,
                currencyCode,
                country,
                calcUnitAmount(currencyCode),
                calcDecimal(currencyCode),
                calcRateValue(rate),
                fetchedAt,
                LocalDateTime.now());
    }

    public static ExchangeRate withId(@NonNull final Long id,
                                      @NonNull final String currencyCode,
                                      @NonNull final Country country,
                                      @NonNull final Integer unitAmount,
                                      @NonNull final Integer decimals,
                                      @NonNull final double rateValue,
                                      @NonNull final LocalDateTime fetchedAt,
                                      @NonNull final LocalDateTime updatedAt) {
        return new ExchangeRate(id, currencyCode, country, unitAmount, decimals, rateValue, fetchedAt, updatedAt);
    }

    private static Double calcRateValue(final Double rate) {
        return BigDecimal
                .valueOf(rate)
                .compareTo(BigDecimal.ZERO) == 0
                ? Double.parseDouble("1") : rate;
    }

    private static int calcUnitAmount(@NonNull final String currencyCode) {
        return hasUnitAmount(currencyCode)
                ? Integer.parseInt(currencyCode.substring(currencyCode.indexOf("(") + 1, currencyCode.indexOf(")")))
                : 1;
    }

    private static boolean hasUnitAmount(@NonNull final String currencyCode) {
        return currencyCode.contains("(") && currencyCode.contains(")");
    }

    private static int calcDecimal(@NonNull final String currencyCode) {
        return (currencyCode.contains("KRW") || currencyCode.contains("JPY")) ? 0 : 2;
    }

    public ExchangeRate updateRate(final double newRateValue) {
        rateValue = calcRateValue(newRateValue);
        updatedAt = LocalDateTime.now();
        return this;
    }
}
