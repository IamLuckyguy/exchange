package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import reactor.core.publisher.Mono;

public interface SaveExchangeRateDegreeCountPort {

    Mono<ExchangeRateDegreeCount> save(ExchangeRateDegreeCount exchangeRateDegreeCount);
}
