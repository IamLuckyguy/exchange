package kr.co.kwt.exchange.adapter.in.openapi.koreaexim;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.*;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "openapi.koreaexim.enable", havingValue = "true")
public class KoreaEximOpenApiClient implements OpenApiClient {

    private final ApiUrlProvider apiUrlProvider;
    private final WebClientService webClientService;

    @Override
    public Flux<OpenApiResponse> call(final OpenApiRequest openApiRequest) {
        return webClientService.getWebClient(apiUrlProvider.getBaseUrl())
                .get()
                .uri(apiUrlProvider.getApiUrl(openApiRequest.getFetchDate().toLocalDate()))
                .retrieve()
                .bodyToFlux(KoreaEximOpenApiResponse.class)
                .cast(OpenApiResponse.class);
    }

    @Override
    public Mono<OpenApiRoundResponse> getRound() {
        return null;
    }
}
