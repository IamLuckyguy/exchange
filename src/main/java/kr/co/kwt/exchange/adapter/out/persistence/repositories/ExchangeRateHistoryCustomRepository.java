package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ExchangeRateHistoryCustomRepository {

    Flux<ExchangeRateHistory> bulkSave(List<ExchangeRateHistory> exchangeRateHistories);
}
