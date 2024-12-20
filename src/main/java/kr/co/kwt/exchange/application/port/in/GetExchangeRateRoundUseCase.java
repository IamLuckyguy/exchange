package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRoundResponse;
import reactor.core.publisher.Mono;

public interface GetExchangeRateRoundUseCase {

    Mono<GetExchangeRateRoundResponse> getExchangeRateRound();
}
