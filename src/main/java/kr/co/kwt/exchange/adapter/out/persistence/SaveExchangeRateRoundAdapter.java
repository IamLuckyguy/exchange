package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateRoundRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateRoundPort;
import kr.co.kwt.exchange.domain.ExchangeRateRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SaveExchangeRateRoundAdapter implements SaveExchangeRateRoundPort {

    private final ExchangeRateRoundRepository exchangeRateRoundRepository;

    @Override
    public Mono<ExchangeRateRound> save(ExchangeRateRound exchangeRateRound) {
        return exchangeRateRoundRepository.save(exchangeRateRound);
    }
}
