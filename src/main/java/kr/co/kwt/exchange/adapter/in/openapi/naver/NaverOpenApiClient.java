package kr.co.kwt.exchange.adapter.in.openapi.naver;

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
@ConditionalOnProperty(name = "openapi.naver.enable", havingValue = "true")
public class NaverOpenApiClient implements OpenApiClient {

    private final NaverApiUrlProvider apiUrlProvider;
    private final WebClientService webClientService;

    @Override
    public Flux<OpenApiResponse> call(final OpenApiRequest openApiRequest) {
        return webClientService
                .getWebClient(apiUrlProvider.getBaseUrl(), new NaverOpenApiWebClientCustomizer())
                .get()
                .retrieve()
                .bodyToFlux(NaverOpenApiBody.class)
                .log("WebClient Request")
                .flatMap(naverOpenApiBody -> Flux.fromIterable(naverOpenApiBody.getResults()))
                .filter(naverOpenApiResponseResult -> naverOpenApiResponseResult.getCurrencyCode() != null)
                .cast(OpenApiResponse.class);
    }
}
