package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ExchangeRateHistoryRepository extends ReactiveCrudRepository<ExchangeRateHistory, Long>, ExchangeRateHistoryCustomRepository {

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByUpdatedAtDesc(String currencyCode);

    Flux<ExchangeRateHistory> findTop365ByCurrencyCodeOrderByFetchedAtDesc(String currencyCode);

    @Query(" SELECT DISTINCT" +
            " FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(erh.fetched_at) / 300) * 300) as fetched_at," +
            " MAX(erh.rate_value) as max_rate_value, " +
            " MIN(erh.rate_value) as min_rate_value, " +
            " AVG(erh.rate_value) as avg_rete_value " +
            " FROM exchange_rate_histories AS erh WHERE currency_code = :currencyCode GROUP BY fetched_at LIMIT 1000")
    Flux<GetExchangeRateResponse.GetExchangeRateHistory> findTop365ByCurrencyCodeOrderByFetchedAtAsc(String currencyCode);
}
