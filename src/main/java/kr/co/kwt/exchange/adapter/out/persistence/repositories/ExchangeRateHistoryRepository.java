package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ExchangeRateHistoryRepository extends ReactiveCrudRepository<ExchangeRateHistory, Long>, ExchangeRateHistoryCustomRepository {

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByUpdatedAtDesc(String currencyCode);

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByFetchedAtDesc(String currencyCode);
}
