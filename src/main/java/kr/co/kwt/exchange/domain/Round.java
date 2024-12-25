package kr.co.kwt.exchange.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private LocalDateTime fetchedAt;

    public static Round init() {
        return Round.builder()
                .round(0)
                .fetchedAt(LocalDateTime.now())
                .build();
    }

    public void updateRound(@NonNull final Integer newRound) {
        round = newRound;
        fetchedAt = LocalDateTime.now();
    }
}
