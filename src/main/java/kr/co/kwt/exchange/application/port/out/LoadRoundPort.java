package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.dto.GetRoundResult;
import kr.co.kwt.exchange.domain.Round;

import java.util.Optional;

public interface LoadRoundPort {

    Optional<Round> findLastRound();

    Optional<GetRoundResult> getLastRoundResult();
}
