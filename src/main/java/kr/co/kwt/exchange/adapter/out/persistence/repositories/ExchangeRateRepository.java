package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long>, ExchangeRateCustomRepository {

    Mono<ExchangeRate> findByCurrencyCode(String currencyCode);

    Flux<GetExchangeRateResponse> GetExchangeRates();

    Mono<GetExchangeRateResponse> GetExchangeRate(GetExchangeRateRequest getExchangeRateRequest);
    
}