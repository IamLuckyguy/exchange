package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Mono;

public interface SaveExchangeRatePort {

    Mono<ExchangeRate> save(ExchangeRate exchangeRate);
}
