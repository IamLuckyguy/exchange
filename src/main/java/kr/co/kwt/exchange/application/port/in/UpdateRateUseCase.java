package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.UpdateRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateResponse;
import reactor.core.publisher.Mono;

public interface UpdateRateUseCase {

    Mono<UpdateRateResponse> updateRate(UpdateRateRequest updateRateRequest);
}
