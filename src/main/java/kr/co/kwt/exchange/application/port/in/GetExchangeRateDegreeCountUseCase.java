package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateDegreeCountResponse;
import reactor.core.publisher.Mono;

public interface GetExchangeRateDegreeCountUseCase {

    Mono<GetExchangeRateDegreeCountResponse> getExchangeRateDegreeCount();
}
