package kr.co.kwt.exchange.adapter.in.api.service;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import kr.co.kwt.exchange.adapter.in.openapi.naver.NaverOpenApiRequest;
import kr.co.kwt.exchange.adapter.out.ExchangeRateWebClientCustomizer;
import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.GetExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateValueUseCase;
import kr.co.kwt.exchange.application.port.in.dto.*;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {

    private static final String SERVICE_NAME = "ExchangeRateApiService";
    private final WebClientService webClientService;
    private final ExchangeRateWebClientCustomizer webClientCustomizer;
    private final UpdateRateValueUseCase updateExchangeRateUseCase;
    private final GetExchangeRateUseCase getExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;
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
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(final FetchExchangeRateRequest request) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate fetchDate = LocalDate.parse(request.getSearchDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime fetchDateTime = LocalDateTime.parse(request.getSearchDate() + " 00:00:00", dateTimeFormatter);

        return openApiClient
                .call(new NaverOpenApiRequest(fetchDate))
                .map(response -> mapToUpdateRateValueRequest(response, fetchDateTime))
                .collectList()
                .filter(list -> !list.isEmpty())
                .flatMapMany(updateExchangeRateUseCase::bulkUpdateRateValues)
                .map(this::mapToFetchExchangeRateResponse);
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
