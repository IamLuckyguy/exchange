package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.RoundRepository;
import kr.co.kwt.exchange.application.port.dto.GetRoundResult;
import kr.co.kwt.exchange.application.port.out.LoadRoundPort;
import kr.co.kwt.exchange.domain.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoadRoundAdapter implements LoadRoundPort {

    private final RoundRepository roundRepository;

    @Override
    public Optional<Round> findLastRound() {
        return roundRepository
                .findAll()
                .stream()
                .findAny();
    }

    @Override
    public Optional<GetRoundResult> getLastRoundResult() {
        return roundRepository.getLastRoundResult();
    }
}
