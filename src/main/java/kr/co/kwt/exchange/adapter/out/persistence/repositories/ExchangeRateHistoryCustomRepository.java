package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import reactor.core.publisher.Mono;

public interface ExchangeRateHistoryCustomRepository {

    Mono<ExchangeRateHistory> find();

    Mono<ExchangeRateHistory> save(ExchangeRateHistory history);

}
