package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import kr.co.kwt.exchange.application.port.dto.GetRoundResult;
import kr.co.kwt.exchange.domain.Round;

import java.util.Optional;

public interface RoundQueryDslRepository {
    Optional<GetRoundResult> getLastRoundResult();

    Optional<Round> getLastRound();
}
