package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiRequest;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverOpenApiClient implements OpenApiClient {

    private final NaverApiUrlProvider apiUrlProvider;
    private final WebClientService webClientService;

    @Override
    public Flux<OpenApiResponse> call(final OpenApiRequest openApiRequest) {
        return webClientService.getWebClient(apiUrlProvider.getBaseUrl())
                .get()
                .retrieve()
                .bodyToFlux(NaverOpenApiBody.class)
                .flatMap(naverOpenApiBody -> Flux.fromIterable(naverOpenApiBody.getResults()))
                .filter(naverOpenApiResponseResult -> naverOpenApiResponseResult.getCurrencyCode() != null)
                .cast(OpenApiResponse.class);
    }
}
