package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueRequest;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UpdateRateValueUseCase {

    Mono<UpdateRateValueResponse> updateRateValue(UpdateRateValueRequest updateRateValueRequest);

    Flux<UpdateRateValueResponse> bulkUpdateRateValues(List<UpdateRateValueRequest> updateRateValueRequests);
}
