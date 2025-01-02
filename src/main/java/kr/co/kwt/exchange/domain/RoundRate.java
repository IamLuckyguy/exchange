package kr.co.kwt.exchange.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Table(name = "round_rates")
@Getter
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class RoundRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_rate_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_code", referencedColumnName = "currencyCode")
    private Exchange exchange;
    private double roundRate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private Round round;
    private String trend;
    private Double trendRate;
    private Double trendDiff;
    private String liveStatus;
    private String marketStatus;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fetchedAt;

    public static RoundRate withoutId(
            @NonNull final Exchange exchange,
            @NonNull final Round round,
            @NonNull final Double roundRate,
            @NonNull final String trend,
            @NonNull final Double trendRate,
            @NonNull final Double trendDiff,
            @NonNull final String liveStatus,
            @NonNull final String marketStatus,
            @NonNull final LocalDateTime fetchedAt
    ) {
        return RoundRate.builder()
                .exchange(exchange)
                .roundRate(roundRate)
                .round(round)
                .trend(trend)
                .trendRate(trendRate)
                .trendDiff(trendDiff)
                .liveStatus((liveStatus == null || liveStatus.isBlank()) ? "실시간" : liveStatus)
                .marketStatus(marketStatus)
                .fetchedAt(fetchedAt)
                .build();
    }
}