package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateHistoryRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateHistoryPort;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
class SaveExchangeRateHistoryAdapter implements SaveExchangeRateHistoryPort {

    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Override
    public Mono<ExchangeRateHistory> save(final ExchangeRateHistory exchangeRateHistory) {
        return exchangeRateHistoryRepository.save(exchangeRateHistory);
    }

}
