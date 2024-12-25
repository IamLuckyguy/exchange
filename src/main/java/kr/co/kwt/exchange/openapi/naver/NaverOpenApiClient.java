package kr.co.kwt.exchange.openapi.naver;

import kr.co.kwt.exchange.config.openapi.NaverOpenApiWebClientCustomizer;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetExchangeResult;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetRoundResult;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "openapi.naver.enable", havingValue = "true")
public class NaverOpenApiClient implements OpenApiClient {

    private final WebClientService webClientService;
    private final NaverOpenApiProperties naverOpenApiProperties;

    @Override
    public OpenApiGetRoundResult getRound() {
        return webClientService
                .getWebClient(naverOpenApiProperties.getRoundUrl(), new NaverOpenApiWebClientCustomizer())
                .get()
                .retrieve()
                .bodyToMono(NaverOpenApiGetRoundBody.class)
                .map(NaverOpenApiGetRoundBody::getResult)
                .cast(OpenApiGetRoundResult.class)
                .block();
    }

    @Override
    public List<OpenApiGetExchangeResult> getExchange(final OpenApiRequest openApiRequest) {
        return webClientService
                .getWebClient(naverOpenApiProperties.getExchangeUrl(), new NaverOpenApiWebClientCustomizer())
                .get()
                .retrieve()
                .bodyToFlux(NaverOpenApiGetExchangeBody.class)
                .flatMap(body -> Flux.fromIterable(body.getResults()))
                .cast(OpenApiGetExchangeResult.class)
                .collectList()
                .block();
    }
}
