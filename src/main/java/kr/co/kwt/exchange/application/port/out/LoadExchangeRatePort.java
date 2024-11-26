package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoadExchangeRatePort {

    Mono<ExchangeRate> findByCountry(String country);

    Flux<ExchangeRate> findAll();

    Mono<ExchangeRate> findByCurrencyCode(String upperCase);
}
