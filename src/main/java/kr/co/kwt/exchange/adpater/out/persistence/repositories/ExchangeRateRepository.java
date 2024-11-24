package kr.co.kwt.exchange.adpater.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
    Mono<ExchangeRate> findByCountry(String country);

//    Flux<ExchangeRate> findBySearchDate(String searchDate);
}