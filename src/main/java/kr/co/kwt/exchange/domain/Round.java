package kr.co.kwt.exchange.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "exchange_rounds")
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Round {

    @Id
    @Column(name = "round_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer round;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime fetchedAt;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    public static Round withoutId(@NonNull final Integer round, @NonNull final LocalDateTime fetchedAt) {
        return Round.builder()
                .round(round)
                .fetchedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Round updateRound(@NonNull final Integer newRound, @NonNull final LocalDateTime newFetchedAt) {
        if (round >= newRound) {
            return Round.withoutId(newRound, newFetchedAt);
        }

        round = newRound;
        fetchedAt = newFetchedAt;
        return this;
    }
}
