package kr.co.kwt.exchange.adapter.in.api.service;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateOpenApiResponse;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.out.ExchangeRateWebClientCustomizer;
import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.GetExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateValueUseCase;
import kr.co.kwt.exchange.application.port.in.dto.*;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {

    private static final String SERVICE_NAME = "ExchangeRateApiService";

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    private final WebClientService webClientService;
    private final ExchangeRateWebClientCustomizer webClientCustomizer;
    private final UpdateRateValueUseCase updateExchangeRateUseCase;
    private final GetExchangeRateUseCase getExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;

    /**
     * 모든 환율 정보 조회 (국가 정보 포함)
     */
    public Flux<GetExchangeRateResponse> searchAllExchangeRateWithCountry() {
        return getExchangeRateUseCase.getExchangeRates();
    }

    /**
     * 환율 정보 조회 (국가 정보 포함)
     */
    public Mono<GetExchangeRateResponse> searchExchangeRateWithCountry(
            final GetExchangeRateRequest request
    ) {
        return getExchangeRateUseCase.getExchangeRate(request);
    }

    /**
     * 환율 정보 추가
     */
    public Mono<AddExchangeRateResponse> addExchangeRate(final AddExchangeRateRequest request) {
        return addExchangeRateUseCase.addExchangeRate(request);
    }

    /**
     * 환율 정보 패치
     */
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final FetchExchangeRateRequest request) {
        return callOpenApi(request)
                .map(this::mapToUpdateRateValueRequest)
                .collectList()
                .flatMapMany(updateExchangeRateUseCase::bulkUpdateRateValues)
                .map(this::mapToFetchExchangeRateResponse);
    }

    private Flux<FetchExchangeRateOpenApiResponse> callOpenApi(final FetchExchangeRateRequest request) {
        return webClientService
                .getWebClient(baseUrl, webClientCustomizer)
                .get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey, request.getSearchDate())
                .retrieve()
                .bodyToFlux(FetchExchangeRateOpenApiResponse.class);
    }

    private UpdateRateValueRequest mapToUpdateRateValueRequest(FetchExchangeRateOpenApiResponse openApiResponse) {
        return new UpdateRateValueRequest(
                openApiResponse.getCurrencyCode(),
                Double.parseDouble(openApiResponse.getTtb().replace(",", "")));
    }

    private FetchExchangeRateResponse mapToFetchExchangeRateResponse(UpdateRateValueResponse updateRateValueResponse) {
        return new FetchExchangeRateResponse(updateRateValueResponse.getCurrencyCode(),
                updateRateValueResponse.getRateValue(),
                updateRateValueResponse.getUpdateTime());
    }
}
