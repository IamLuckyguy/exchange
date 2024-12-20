package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.UpdateExchangeRoundUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateRoundPort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateRoundPort;
import kr.co.kwt.exchange.domain.ExchangeRateRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateExchangeRoundService implements UpdateExchangeRoundUseCase {

    private final LoadExchangeRateRoundPort loadExchangeRateRoundPort;
    private final SaveExchangeRateRoundPort saveExchangeRateRoundPort;

    @Override
    public Mono<Integer> updateDegreeCount(int currentRound) {
        return loadExchangeRateRoundPort
                .findFirstByOrderByFetchedAtDesc()
                .flatMap(exchangeRateRound -> {
                    exchangeRateRound.fetch(currentRound);
                    return saveExchangeRateRoundPort
                            .save(exchangeRateRound)
                            .map(ExchangeRateRound::getDegreeCount);
                });
    }
}
