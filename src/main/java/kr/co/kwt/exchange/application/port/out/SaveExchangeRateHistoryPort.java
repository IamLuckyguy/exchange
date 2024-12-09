package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SaveExchangeRateHistoryPort {
    Mono<ExchangeRateHistory> save(ExchangeRateHistory exchangeRateHistory);

    Flux<ExchangeRateHistory> saveAll(Flux<ExchangeRateHistory> exchangeRateFlux);

    Flux<ExchangeRateHistory> bulkSave(List<ExchangeRateHistory> exchangeRateFlux);

}
