package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateHistoryCustomRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateHistoryPort;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
class SaveExchangeRateHistoryAdapter implements SaveExchangeRateHistoryPort {

    private final ExchangeRateHistoryCustomRepository exchangeRateCustomRepository;

    @Override
    public Mono<ExchangeRateHistory> save(final ExchangeRateHistory exchangeRateHistory) {
        return exchangeRateCustomRepository.save(exchangeRateHistory);
    }

}
