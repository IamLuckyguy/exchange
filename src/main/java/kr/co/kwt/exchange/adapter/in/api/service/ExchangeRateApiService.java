package kr.co.kwt.exchange.adapter.in.api.service;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiRoundResponse;
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
    private final GetExchangeRateRoundUseCase getExchangeRateRoundUseCase;
    private final AddExchangeRateRoundUseCase addExchangeRateRoundUseCase;
    private final UpdateExchangeRoundUseCase updateExchangeRoundUseCase;
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
                .getRound()
                .flatMapMany(degreeCountResponse -> handleExchangeRateRound(degreeCountResponse, fetchDate))
                .flatMap(openApiClient -> openApiClient.call(new NaverOpenApiRequest()))
                .map(response -> mapToUpdateRateValueRequest(response, fetchDate.atStartOfDay()))
                .collectList()
                .filter(list -> !list.isEmpty())
                .flatMapMany(updateExchangeRateUseCase::bulkUpdateRateValues)
                .map(this::mapToFetchExchangeRateResponse);
    }

    private Mono<OpenApiClient> handleExchangeRateRound(final OpenApiRoundResponse response,
                                                        final LocalDate fetchDate
    ) {
        return validateDegreeCount(fetchDate)
                .flatMap(queryResponse -> doUpdateRound(queryResponse, response))
                .then(Mono.just(openApiClient));
    }

    private Mono<GetExchangeRateRoundResponse> validateDegreeCount(final LocalDate fetchDate) {
        return getExchangeRateRoundUseCase
                .getExchangeRateRound()
                .switchIfEmpty(Mono.just(new GetExchangeRateRoundResponse(1, fetchDate.atStartOfDay())));
    }

    private Mono<Integer> doUpdateRound(final GetExchangeRateRoundResponse queryResponse,
                                        final OpenApiRoundResponse openApiRoundResponse
    ) {
        return doUpdateDegreeCount(queryResponse, openApiRoundResponse);
    }

    private Mono<Integer> doUpdateDegreeCount(final GetExchangeRateRoundResponse queryResponse,
                                              final OpenApiRoundResponse openApiRoundResponse
    ) {
        return openApiRoundResponse.getRound() > queryResponse.getDegreeCount()
                ? updateExchangeRoundUseCase.updateDegreeCount(openApiRoundResponse.getRound())
                : Mono.empty();
    }

    private UpdateRateValueRequest mapToUpdateRateValueRequest(final OpenApiResponse openApiResponse,
                                                               final LocalDateTime fetchedAt
    ) {
        return new UpdateRateValueRequest(
                openApiResponse.getCurrencyCode(),
                openApiResponse.getRateValue(),
                fetchedAt);
    }

    private FetchExchangeRateResponse mapToFetchExchangeRateResponse(
            final UpdateRateValueResponse updateRateValueResponse
    ) {
        return new FetchExchangeRateResponse(updateRateValueResponse.getCurrencyCode(),
                updateRateValueResponse.getRateValue(),
                updateRateValueResponse.getUpdateTime());
    }
}
