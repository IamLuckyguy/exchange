package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.UpdateDegreeCountUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateDegreeCountPort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateDegreeCountPort;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateDegreeCountService implements UpdateDegreeCountUseCase {

    private final LoadExchangeRateDegreeCountPort loadExchangeRateDegreeCountPort;
    private final SaveExchangeRateDegreeCountPort saveExchangeRateDegreeCountPort;

    @Override
    public Mono<Integer> updateDegreeCount(int currentDegreeCount) {
        return loadExchangeRateDegreeCountPort
                .findFirstByOrderByFetchedAtDesc()
                .flatMap(exchangeRateDegreeCount -> {
                    exchangeRateDegreeCount.fetch(currentDegreeCount);
                    return saveExchangeRateDegreeCountPort
                            .save(exchangeRateDegreeCount)
                            .map(ExchangeRateDegreeCount::getDegreeCount);
                });
    }
}
