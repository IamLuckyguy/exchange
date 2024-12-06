package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import reactor.core.publisher.Mono;

public interface SaveExchangeRateHistoryPort {
    Mono<ExchangeRateHistory> save(ExchangeRateHistory exchangeRateHistory);
}
