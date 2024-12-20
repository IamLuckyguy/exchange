package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateDegreeCountResponse;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import reactor.core.publisher.Mono;

public interface LoadExchangeRateDegreeCountPort {

    Mono<GetExchangeRateDegreeCountResponse> getExchangeRateDegreeCount();

    Mono<ExchangeRateDegreeCount> findFirstByOrderByFetchedAtDesc();

}
