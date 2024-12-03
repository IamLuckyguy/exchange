package kr.co.kwt.exchange.adpater.in.api.service;

import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateOpenApiResponse;
import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.utils.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;
    private final UpdateRateUseCase updateExchangeRateUseCase;
    private final SearchExchangeRateUseCase searchExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;

    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
        return searchExchangeRateUseCase.searchAllExchangeRateWithCountry();
    }

    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(
            final SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest
    ) {
        return searchExchangeRateUseCase.searchExchangeRateWithCountry(searchExchangeRateWithCountryRequest);
    }

    public Mono<AddExchangeRateResponse> addExchangeRate(final AddExchangeRateRequest addExchangeRateRequest) {
        return addExchangeRateUseCase.addExchangeRate(addExchangeRateRequest);
    }

    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final FetchExchangeRateRequest fetchExchangeRateRequest) {
        WebClient webClient = WebClientUtils.getOrCreateWebClient(webClientBuilder, baseUrl);

        return webClient.get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey,
                        fetchExchangeRateRequest.getSearchDate())
                .retrieve()
                .bodyToFlux(FetchExchangeRateOpenApiResponse.class)
                .filter(response -> response.getResult() == 1)
                .flatMap(this::updateRates);
    }

    private Flux<FetchExchangeRateResponse> updateRates(
            final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse
    ) {
        return searchExchangeRateUseCase
                .searchExchangeRate(fetchExchangeRateOpenApiResponse.getCurrencyCode())
                .transform(addExchangeRateUseCase.addExchangeRate());
    }
}
