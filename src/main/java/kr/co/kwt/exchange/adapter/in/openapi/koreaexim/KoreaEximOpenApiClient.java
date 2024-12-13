package kr.co.kwt.exchange.adapter.in.openapi.koreaexim;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.ApiUrlProvider;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiRequest;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
                .uri(apiUrlProvider.getPath(openApiRequest.getFetchDate().toLocalDate()))
                .retrieve()
                .bodyToFlux(KoreaEximOpenApiResponse.class)
                .cast(OpenApiResponse.class);
    }
}
