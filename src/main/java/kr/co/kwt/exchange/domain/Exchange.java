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
import java.util.Optional;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", referencedColumnName = "currencyCode")
    private List<RoundRate> dailyRoundRates;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", referencedColumnName = "currencyCode")
    private List<ClosingRate> yearlyClosingRates;

    private int unit;
    private int decimals;
    private LocalDateTime createdAt;
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

    public void updateDecimals(final int decimals) {
        this.decimals = decimals;
    }

    public void updateUnit(final int unit) {
        this.unit = unit;
    }

    public void addAllYearlyClosingRates(@NonNull final List<ClosingRate> addedYearlyClosingRates) {
        yearlyClosingRates.addAll(addedYearlyClosingRates);
    }

    public void addYearlyClosingRate(@NonNull final ClosingRate yearlyClosingRate) {
        yearlyClosingRates.add(yearlyClosingRate);
    }

    public void fetch(
            @NonNull final Double fetchRate,
            @NonNull final Integer fetchRound,
            @NonNull final String trend,
            @NonNull final Double trendRate,
            @NonNull final String liveStatus,
            @NonNull final String marketStatus,
            @NonNull final LocalDateTime fetchedAt
    ) {
        if (fetchRound == FIRST_ROUND) {
            getLastRoundRate().ifPresent(roundRate -> fetchClosingRate(roundRate.getRoundRate(), fetchedAt));
        }

        fetchRoundRate(fetchRate, fetchRound, trend, trendRate, liveStatus, marketStatus, fetchedAt);
    }

    private void fetchRoundRate(
            @NonNull final Double fetchRate,
            @NonNull final Integer fetchRound,
            @NonNull final String trend,
            @NonNull final Double trendRate,
            @NonNull final String liveStatus,
            @NonNull final String marketStatus,
            @NonNull final LocalDateTime fetchedAt
    ) {
        dailyRoundRates.add(RoundRate.withoutId(
                currencyCode,
                fetchRound,
                fetchRate,
                trend,
                trendRate,
                liveStatus,
                marketStatus,
                fetchedAt));
    }

    private void fetchClosingRate(
            @NonNull final Double lastRoundRate,
            @NonNull final LocalDateTime fetchedAt
    ) {
        yearlyClosingRates.add(ClosingRate.withoutId(lastRoundRate, fetchedAt));
    }

    public Optional<RoundRate> getLastRoundRate() {
        return Optional.ofNullable(dailyRoundRates
                .get(dailyRoundRates.size() - 1));
    }
}
