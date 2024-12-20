package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateDegreeCountRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateDegreeCountPort;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SaveExchangeRateDegreeCountAdapter implements SaveExchangeRateDegreeCountPort {

    private final ExchangeRateDegreeCountRepository exchangeRateDegreeCountRepository;

    @Override
    public Mono<ExchangeRateDegreeCount> save(ExchangeRateDegreeCount exchangeRateDegreeCount) {
        return exchangeRateDegreeCountRepository.save(exchangeRateDegreeCount);
    }
}
