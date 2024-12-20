package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OpenApiClient {

    Flux<OpenApiResponse> call(OpenApiRequest openApiRequest);

    Mono<OpenApiDegreeCountResponse> getDegreeCount();

}
