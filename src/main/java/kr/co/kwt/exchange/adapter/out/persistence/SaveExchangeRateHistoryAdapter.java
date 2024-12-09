package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateHistoryRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateHistoryPort;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
class SaveExchangeRateHistoryAdapter implements SaveExchangeRateHistoryPort {

    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Override
    public Mono<ExchangeRateHistory> save(final ExchangeRateHistory exchangeRateHistory) {
        return exchangeRateHistoryRepository.save(exchangeRateHistory);
    }

    @Override
    public Flux<ExchangeRateHistory> saveAll(final Flux<ExchangeRateHistory> exchangeRateHistories) {
        return exchangeRateHistoryRepository.saveAll(exchangeRateHistories);
    }

    @Override
    public Flux<ExchangeRateHistory> bulkSave(final List<ExchangeRateHistory> exchangeRateHistories) {
        return exchangeRateHistoryRepository.bulkSave(exchangeRateHistories);
    }

}
