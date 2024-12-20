package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateDegreeCountResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AddExchangeRateDegreeCountUseCase {

    Mono<AddExchangeRateDegreeCountResponse> addExchangeRateDegreeCount(LocalDateTime localDateTime);

}
