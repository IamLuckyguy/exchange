package kr.co.kwt.exchange.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "exchanges")
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Exchange {

    public static final int FIRST_ROUND = 1;

    @Id
    @Column(name = "exchange_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    private Country country;

    @OrderBy("fetchedAt DESC")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "exchange")
    private List<RoundRate> dailyRoundRates;

    @OrderBy("fetchedAt DESC")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "exchange")
    private List<ClosingRate> yearlyClosingRates;

    private int unit;
    private int decimals;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    public static Exchange withoutId(
            @NonNull final String currencyCode,
            @NonNull final Country country,
            @NonNull final Integer unit,
            @NonNull final Integer decimals
    ) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Exchange.builder()
                .currencyCode(currencyCode)
                .country(country)
                .dailyRoundRates(new ArrayList<>())
                .yearlyClosingRates(new ArrayList<>())
                .unit(unit)
                .decimals(decimals)
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
    }

    public static Exchange withId(
            @NonNull final Long id,
            @NonNull final String currencyCode,
            @NonNull final Country country,
            @NonNull final List<RoundRate> dailyRoundRates,
            @NonNull final List<ClosingRate> yearlyClosingRates,
            @NonNull final Integer unit,
            @NonNull final Integer decimals,
            @NonNull final LocalDateTime createdAt,
            @NonNull final LocalDateTime updatedAt
    ) {
        return Exchange.builder()
                .id(id)
                .currencyCode(currencyCode)
                .country(country)
                .dailyRoundRates(dailyRoundRates)
                .yearlyClosingRates(yearlyClosingRates)
                .unit(unit)
                .decimals(decimals)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void updateDecimals(@NonNull final Integer newDecimals) {
        decimals = newDecimals;
    }

    public void updateUnit(@NonNull final Integer newUnit) {
        unit = newUnit;
    }

    public void addAllDailyRoundRates(@NonNull final List<RoundRate> newDailyRoundRates) {
        dailyRoundRates = newDailyRoundRates;
    }

    public void addAllYearlyClosingRates(@NonNull final List<ClosingRate> newYearlyClosingRates) {
        yearlyClosingRates = newYearlyClosingRates;
    }
}
