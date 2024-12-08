package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long>, ExchangeRateCustomRepository {

}