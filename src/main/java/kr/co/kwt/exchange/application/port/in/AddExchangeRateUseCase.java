package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import reactor.core.publisher.Mono;

public interface AddExchangeRateUseCase {

    Mono<AddExchangeRateResponse> addExchangeRate(AddExchangeRateRequest addExchangeRateRequest);
}
