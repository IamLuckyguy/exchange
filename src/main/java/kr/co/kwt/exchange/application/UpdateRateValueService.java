package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.UpdateRateValueUseCase;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueRequest;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateHistoryPort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateRateValueService implements UpdateRateValueUseCase {

    private final LoadExchangeRatePort loadExchangeRatePort;
    private final SaveExchangeRatePort saveExchangeRatePort;
    private final SaveExchangeRateHistoryPort saveExchangeRateHistoryPort;

    /**
     * 단일 환율값 업데이트
     *
     * @param request
     * @return
     */
    @Override
    public Mono<UpdateRateValueResponse> updateRateValue(@NonNull final UpdateRateValueRequest request) {
        return loadExchangeRatePort
                .findByCurrencyCode(request.getCurrencyCode())
                .switchIfEmpty(Mono.error(InvalidCurrencyCodeException::new))
                .flatMap(exchangeRate -> doUpdateRateValue(exchangeRate, request.getRateValue(), request.getFetchedAt()))
                .map(UpdateRateValueResponse::of);
    }

    private Mono<ExchangeRate> doUpdateRateValue(@NonNull final ExchangeRate exchangeRate,
                                                 final double rateValue,
                                                 @NonNull LocalDateTime fetchedAt
    ) {
        return Mono
                .just(exchangeRate.updateRate(rateValue, fetchedAt))
                .flatMap(saveExchangeRatePort::save)
                .map(ExchangeRateHistory::of)
                .flatMap(saveExchangeRateHistoryPort::save)
                .then(Mono.just(exchangeRate));
    }

    /**
     * 다중 환율값 업데이트, 최초 환율 업데이트시 환율 도메인 등록
     *
     * @param requests
     * @return
     */
    @Override
    public Flux<UpdateRateValueResponse> bulkUpdateRateValues(@NonNull final List<UpdateRateValueRequest> requests) {
        return loadExchangeRatePort
                .findAllByCurrencyCode(getCurrencyCodesFrom(requests))
                .collectList()
                .flatMapMany(exchangeRates -> Flux.fromIterable(ensureExistsExchangeRate(exchangeRates, requests)))
                .flatMap(exchangeRate -> doUpdateExchangeRate(exchangeRate, requests))
                .collectList()
                .flatMapMany(this::doBulkSave)
                .map(UpdateRateValueResponse::of);
    }

    private List<String> getCurrencyCodesFrom(@NonNull final List<UpdateRateValueRequest> requests) {
        return requests
                .stream()
                .map(UpdateRateValueRequest::getCurrencyCode)
                .toList();
    }

    /**
     * DB 조회시 없는 경우, 신규 ExchangeRate 생성하여, ExchangeRate 처리시 있음을 보장한다.
     */
    private List<ExchangeRate> ensureExistsExchangeRate(@NonNull final List<ExchangeRate> exchangeRates,
                                                        @NonNull final List<UpdateRateValueRequest> requests
    ) {
        LocalDateTime fetchedAt = getFetchedAt(requests);
        Map<String, Double> exchangeRateCurrencyCodeMap = getExchangeRatesCurrencyCodeMap(exchangeRates);
        Map<String, Double> requestCurrencyCodeMap = getRequestCurrencyCodeMap(requests);

        requests.stream()
                .map(UpdateRateValueRequest::getCurrencyCode)
                .filter(currencyCode -> !exchangeRateCurrencyCodeMap.containsKey(currencyCode))
                .forEach(currencyCode -> exchangeRates.add(ExchangeRate.withoutId(
                        null, currencyCode, requestCurrencyCodeMap.get(currencyCode), fetchedAt)));

        return exchangeRates;
    }

    /**
     * 환율 값을 업데이트한다.
     */
    private Flux<ExchangeRate> doUpdateExchangeRate(@NonNull final ExchangeRate exchangeRate,
                                                    @NonNull final List<UpdateRateValueRequest> requests
    ) {
        return Flux.just(exchangeRate.updateRate(
                getRequestCurrencyCodeMap(requests).get(exchangeRate.getCurrencyCode()),
                getFetchedAt(requests)));
    }

    /**
     * ExchangeRate 도메인 저장 또는 업데이트, ExchangeRate ExchangeRateHistory 저장
     */
    private Flux<ExchangeRateHistory> doBulkSave(@NonNull final List<ExchangeRate> exchangeRates) {
        return saveExchangeRatePort
                .bulkSave(exchangeRates)
                .map(ExchangeRateHistory::of)
                .collectList()
                .flatMapMany(saveExchangeRateHistoryPort::bulkSave);
    }

    /**
     * UpdateRateValueRequest 리스트를 기준으로 (CurrencyCode, RateValue) 맵을 생성한다.
     */
    private Map<String, Double> getRequestCurrencyCodeMap(@NonNull final List<UpdateRateValueRequest> requests) {
        return requests
                .stream()
                .collect(toMap(UpdateRateValueRequest::getCurrencyCode, UpdateRateValueRequest::getRateValue));
    }

    /**
     * DB 조회된 ExchangeRate 리스트를 기준으로 (CurrencyCode, RateValue) 맵을 생성한다.
     */
    private Map<String, Double> getExchangeRatesCurrencyCodeMap(@NonNull final List<ExchangeRate> exchangeRates) {
        return exchangeRates
                .stream()
                .collect(toMap(ExchangeRate::getCurrencyCode, ExchangeRate::getRateValue));
    }

    private LocalDateTime getFetchedAt(@NonNull final List<UpdateRateValueRequest> requests) {
        return requests
                .stream()
                .map(UpdateRateValueRequest::getFetchedAt)
                .findFirst()
                .orElseThrow();
    }
}
