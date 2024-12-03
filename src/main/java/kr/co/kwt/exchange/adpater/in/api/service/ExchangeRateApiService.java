package kr.co.kwt.exchange.adpater.in.api.service;

import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateOpenApiResponse;
import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adpater.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.*;
import kr.co.kwt.exchange.domain.ExchangeRate;
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

    /**
     * 모든 환율 정보 조회 (국가 정보 포함)
     */
    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
        return searchExchangeRateUseCase.searchAllExchangeRateWithCountry();
    }

    /**
     * 환율 정보 조회 (국가 정보 포함)
     */
    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(
            final SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest
    ) {
        return searchExchangeRateUseCase.searchExchangeRateWithCountry(searchExchangeRateWithCountryRequest);
    }

    /**
     * 환율 정보 추가
     */
    public Mono<AddExchangeRateResponse> addExchangeRate(final AddExchangeRateRequest addExchangeRateRequest) {
        return addExchangeRateUseCase.addExchangeRate(addExchangeRateRequest);
    }

    /**
     * 환율 정보 패치
     */
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final FetchExchangeRateRequest fetchExchangeRateRequest) {
        WebClient webClient = WebClientUtils.getOrCreateWebClient(webClientBuilder, baseUrl);

        return webClient.get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey, fetchExchangeRateRequest.getSearchDate())
                .retrieve()
                .bodyToFlux(FetchExchangeRateOpenApiResponse.class)
                .filter(this::isSuccessApiResponse)
                .flatMap(this::updateExchangeRateValue)
                .map(this::generateFetchExchangeRateResponse);
    }

    private boolean isSuccessApiResponse(final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        return fetchExchangeRateOpenApiResponse.getResult() == 1;
    }

    private Mono<ExchangeRate> updateExchangeRateValue(
            final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse
    ) {
        return searchExchangeRateUseCase
                .searchExchangeRate(fetchExchangeRateOpenApiResponse.getCurrencyCode())
                .switchIfEmpty(Mono.just(ExchangeRate.withoutId(fetchExchangeRateOpenApiResponse.getCurrencyCode(),
                        getUnitAmount(fetchExchangeRateOpenApiResponse),
                        getRateValue(fetchExchangeRateOpenApiResponse))))
                .flatMap(exchangeRate -> doUpdateExchangeRateValue(exchangeRate,
                        fetchExchangeRateOpenApiResponse));
    }

    private FetchExchangeRateResponse generateFetchExchangeRateResponse(final ExchangeRate exchangeRate) {
        return new FetchExchangeRateResponse(exchangeRate.getId(),
                exchangeRate.getCurrencyCode(),
                exchangeRate.getRateValue(),
                exchangeRate.getUpdatedAt());
    }

    private Mono<ExchangeRate> doUpdateExchangeRateValue(
            final ExchangeRate exchangeRate,
            final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse
    ) {
        if (exchangeRate.getId() == null) {
            return save(fetchExchangeRateOpenApiResponse)
                    .then(update(fetchExchangeRateOpenApiResponse));
        }

        return update(fetchExchangeRateOpenApiResponse);
    }

    private Mono<ExchangeRate> update(final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        return updateExchangeRateUseCase
                .updateRate(new UpdateRateRequest(
                        fetchExchangeRateOpenApiResponse.getCurrencyCode(),
                        getRateValue(fetchExchangeRateOpenApiResponse)))
                .then(searchExchangeRateUseCase.searchExchangeRate(fetchExchangeRateOpenApiResponse.getCurrencyCode()));
    }

    private Mono<ExchangeRate> save(final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        return addExchangeRateUseCase
                .addExchangeRate(new AddExchangeRateRequest(
                        fetchExchangeRateOpenApiResponse.getCurrencyCode(),
                        getUnitAmount(fetchExchangeRateOpenApiResponse),
                        getRateValue(fetchExchangeRateOpenApiResponse)))
                .then(update(fetchExchangeRateOpenApiResponse));
    }

    private int getUnitAmount(final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        String currencyCode = fetchExchangeRateOpenApiResponse.getCurrencyCode();

        return currencyCode.indexOf("(") > 0 ?
                Integer.parseInt(currencyCode.substring(currencyCode.indexOf("(") + 1, currencyCode.indexOf(")")))
                : 0;
    }

    private double getRateValue(FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        return Double.parseDouble(fetchExchangeRateOpenApiResponse.getTtb().replace(",", ""));
    }
}
