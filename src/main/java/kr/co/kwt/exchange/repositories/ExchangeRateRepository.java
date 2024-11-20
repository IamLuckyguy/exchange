package kr.co.kwt.exchange.repositories;

import kr.co.kwt.exchange.models.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import java.time.LocalDate;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
    Flux<ExchangeRate> findBySearchDate(String searchDate);
}