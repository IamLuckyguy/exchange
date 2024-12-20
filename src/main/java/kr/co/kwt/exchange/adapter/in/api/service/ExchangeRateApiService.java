package kr.co.kwt.exchange.adapter.in.api.service;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiDegreeCountResponse;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import kr.co.kwt.exchange.adapter.in.openapi.naver.NaverOpenApiRequest;
import kr.co.kwt.exchange.application.port.in.*;
import kr.co.kwt.exchange.application.port.in.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {

    private static final String SERVICE_NAME = "ExchangeRateApiService";
    private final UpdateRateValueUseCase updateExchangeRateUseCase;
    private final GetExchangeRateUseCase getExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;
    private final GetExchangeRateDegreeCountUseCase getExchangeRateDegreeCountUseCase;
    private final AddExchangeRateDegreeCountUseCase addExchangeRateDegreeCountUseCase;
    private final UpdateDegreeCountUseCase updateDegreeCountUseCase;
    private final OpenApiClient openApiClient;

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
    @Transactional
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final LocalDate fetchDate) {
        return openApiClient
                .getDegreeCount()
                .flatMapMany(degreeCountResponse -> handleDegreeCount(degreeCountResponse, fetchDate))
                .flatMap(openApiClient -> openApiClient.call(new NaverOpenApiRequest()))
                .map(response -> mapToUpdateRateValueRequest(response, fetchDate.atStartOfDay()))
                .collectList()
                .filter(list -> !list.isEmpty())
                .flatMapMany(updateExchangeRateUseCase::bulkUpdateRateValues)
                .map(this::mapToFetchExchangeRateResponse);
    }

    private Mono<OpenApiClient> handleDegreeCount(final OpenApiDegreeCountResponse response,
                                                  final LocalDate fetchDate) {
        return validateDegreeCount(fetchDate)
                .flatMap(queryResponse -> updateDegreeCount(queryResponse, response))
                .then(Mono.just(openApiClient));
    }

    private Mono<Integer> updateDegreeCount(final GetExchangeRateDegreeCountResponse queryResponse,
                                            final OpenApiDegreeCountResponse response) {
        return doUpdateDegreeCount(queryResponse, response);
    }

    private Mono<GetExchangeRateDegreeCountResponse> validateDegreeCount(final LocalDate fetchDate) {
        return getExchangeRateDegreeCountUseCase
                .getExchangeRateDegreeCount()
                .switchIfEmpty(Mono.just(new GetExchangeRateDegreeCountResponse(1, fetchDate.atStartOfDay())));
    }

    private Mono<Integer> doUpdateDegreeCount(final GetExchangeRateDegreeCountResponse queryResponse,
                                              final OpenApiDegreeCountResponse openApiDegreeCountResponse
    ) {
        if (openApiDegreeCountResponse.getDegreeCount() > queryResponse.getDegreeCount()) {
            return updateDegreeCountUseCase.updateDegreeCount(openApiDegreeCountResponse.getDegreeCount());
        }
        else {
            return Mono.empty();
        }
    }

    private UpdateRateValueRequest mapToUpdateRateValueRequest(final OpenApiResponse openApiResponse,
                                                               final LocalDateTime fetchedAt
    ) {
        return new UpdateRateValueRequest(
                openApiResponse.getCurrencyCode(),
                openApiResponse.getRateValue(),
                fetchedAt);
    }

    private FetchExchangeRateResponse mapToFetchExchangeRateResponse(final UpdateRateValueResponse updateRateValueResponse) {
        return new FetchExchangeRateResponse(updateRateValueResponse.getCurrencyCode(),
                updateRateValueResponse.getRateValue(),
                updateRateValueResponse.getUpdateTime());
    }
}
