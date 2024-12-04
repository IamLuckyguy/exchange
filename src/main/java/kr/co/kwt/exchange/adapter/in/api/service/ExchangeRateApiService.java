package kr.co.kwt.exchange.adapter.in.api.service;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateOpenApiResponse;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.out.ExchangeRateWebClientCustomizer;
import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.*;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import kr.co.kwt.exchange.domain.ExchangeRate;
import kr.co.kwt.exchange.utils.LogUtils;
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
    private static final String SERVICE_NAME = "ExchangeRateApiService";

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    private final WebClientService webClientService;
    private final ExchangeRateWebClientCustomizer webClientCustomizer;
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
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final FetchExchangeRateRequest request) {
        WebClient webClient = webClientService.getWebClient(baseUrl, webClientCustomizer);
        String requestDate = request.getSearchDate();
        LogUtils.logApiRequest(SERVICE_NAME, "fetchExchangeRates", requestDate);
        long startTime = System.currentTimeMillis();

        return webClient.get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey, requestDate)
                .retrieve()
                .bodyToFlux(FetchExchangeRateOpenApiResponse.class)
                .doOnNext(response -> LogUtils.logApiResponse(SERVICE_NAME, "fetchExchangeRates", response))
                .filter(this::isSuccessApiResponse)
                .doOnNext(response -> LogUtils.logBusinessOperation(
                        SERVICE_NAME,
                        "FETCH",
                        response.getCurrencyCode(),
                        "SUCCESS"
                ))
                .flatMap(this::updateExchangeRateValue)
                .map(this::generateFetchExchangeRateResponse)
                .doOnComplete(() -> log.info("Completed fetching exchange rates for date: {}", requestDate))
                .doOnError(error -> LogUtils.logError(SERVICE_NAME, "fetchExchangeRates", error));
    }

    private boolean isSuccessApiResponse(final FetchExchangeRateOpenApiResponse fetchExchangeRateOpenApiResponse) {
        return fetchExchangeRateOpenApiResponse.getResult() == 1;
    }

    private Mono<ExchangeRate> updateExchangeRateValue(FetchExchangeRateOpenApiResponse response) {
        LogUtils.logDebug(SERVICE_NAME, "updateExchangeRateValue",
                "Updating exchange rate for currency: %s", response.getCurrencyCode());

        return searchExchangeRateUseCase
                .searchExchangeRate(response.getCurrencyCode())
                .doOnNext(rate -> LogUtils.logDebug(SERVICE_NAME, "updateExchangeRateValue",
                        "Found existing rate for currency %s: %s",
                        response.getCurrencyCode(), rate))
                .switchIfEmpty(Mono.defer(() -> {
                    LogUtils.logBusinessOperation(SERVICE_NAME, "CREATE",
                            response.getCurrencyCode(), "NEW_ENTRY");
                    return Mono.just(ExchangeRate.withoutId(
                            response.getCurrencyCode(),
                            getUnitAmount(response),
                            getRateValue(response)));
                }))
                .flatMap(exchangeRate -> doUpdateExchangeRateValue(exchangeRate, response))
                .doOnError(error -> LogUtils.logError(SERVICE_NAME, "updateExchangeRateValue",
                        "Failed to update exchange rate for currency: %s", response.getCurrencyCode()));
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
