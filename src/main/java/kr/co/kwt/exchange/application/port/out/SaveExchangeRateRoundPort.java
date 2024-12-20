package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRateRound;
import reactor.core.publisher.Mono;

public interface SaveExchangeRateRoundPort {

    Mono<ExchangeRateRound> save(ExchangeRateRound exchangeRateRound);
}
