package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRoundResponse;
import kr.co.kwt.exchange.domain.ExchangeRateRound;
import reactor.core.publisher.Mono;

public interface LoadExchangeRateRoundPort {

    Mono<GetExchangeRateRoundResponse> getExchangeRateRound();

    Mono<ExchangeRateRound> findFirstByOrderByFetchedAtDesc();

}
