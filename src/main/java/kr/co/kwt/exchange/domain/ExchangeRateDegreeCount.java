package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table("exchange_rates_degree_counts")
public class ExchangeRateDegreeCount {

    @Id
    private Long id;
    private int degreeCount;
    private LocalDateTime fetchedAt;

    public static ExchangeRateDegreeCount withoutId() {
        return new ExchangeRateDegreeCount(null, 1, null);
    }

    public int fetch(final int currentDegreeCount) {
        degreeCount = currentDegreeCount;
        return degreeCount;
    }
}
