package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRoundResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AddExchangeRateRoundUseCase {

    Mono<AddExchangeRateRoundResponse> addExchangeRateDegreeCount(LocalDateTime localDateTime);

}
