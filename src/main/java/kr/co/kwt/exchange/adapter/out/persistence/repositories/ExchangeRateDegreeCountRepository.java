package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateDegreeCountResponse;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRateDegreeCountRepository extends ReactiveCrudRepository<ExchangeRateDegreeCount, Long> {

    @Query("SELECT erdc.degree_count, erdc.fetched_at FROM exchange_rates_degree_counts AS erdc")
    Mono<GetExchangeRateDegreeCountResponse> getExchangeRateDegreeCount();

    Mono<ExchangeRateDegreeCount> findFirstByOrderByFetchedAtDesc();
}
