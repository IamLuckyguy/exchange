package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateHistoryResponse;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ExchangeRateHistoryRepository extends ReactiveCrudRepository<ExchangeRateHistory, Long>, ExchangeRateHistoryCustomRepository {

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByUpdatedAtDesc(String currencyCode);

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByFetchedAtDesc(String currencyCode);

    @Query(" SELECT DISTINCT" +
            " erh.fetched_at as fetched_at," +
            " erh.rate_value as rate_value " +
            " FROM exchange_rate_histories AS erh " +
            " WHERE currency_code = :currencyCode " +
            " LIMIT 1000")
    Flux<GetExchangeRateHistoryResponse> findTop365ByCurrencyCodeOrderByFetchedAtAsc(String currencyCode);
}
