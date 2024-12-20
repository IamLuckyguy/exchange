package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateRoundRepository;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRoundResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateRoundPort;
import kr.co.kwt.exchange.domain.ExchangeRateRound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoadExchangeRateRoundAdapter implements LoadExchangeRateRoundPort {

    private final ExchangeRateRoundRepository exchangeRateRoundRepository;

    @Override
    public Mono<GetExchangeRateRoundResponse> getExchangeRateRound() {
        return exchangeRateRoundRepository.getExchangeRateDegreeCount();
    }

    @Override
    public Mono<ExchangeRateRound> findFirstByOrderByFetchedAtDesc() {
        return exchangeRateRoundRepository.findFirstByOrderByFetchedAtDesc();
    }

}
