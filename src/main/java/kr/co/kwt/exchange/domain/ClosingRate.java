package kr.co.kwt.exchange.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "closing_rates")
@Getter
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class ClosingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closing_rate_id")
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;
    private double closingRate;
    private LocalDateTime fetchedAt;

    public static ClosingRate withoutId(
            @NonNull final String currencyCode,
            @NonNull final Double closingRate,
            @NonNull final LocalDateTime fetchedAt
    ) {
        return ClosingRate.builder()
                .currencyCode(currencyCode)
                .closingRate(closingRate)
                .fetchedAt(fetchedAt)
                .build();
    }
}