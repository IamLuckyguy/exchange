package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import reactor.core.publisher.Flux;

public interface OpenApiClient {

    Flux<OpenApiResponse> call(OpenApiRequest openApiRequest);
}
