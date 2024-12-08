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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

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
                .flatMap(exchangeRate -> doUpdateRateValue(exchangeRate, request.getRateValue()))
                .map(UpdateRateValueResponse::of);
    }

    /**
     * 다중 환율값 업데이트, 최초 환율 업데이트시 환율 도메인 등록
     *
     * @param requests
     * @return
     */
    @Override
    public Flux<UpdateRateValueResponse> bulkUpdateRateValues(@NonNull final List<UpdateRateValueRequest> requests) {
        Map<String, Double> currencyCodeMap = requests
                .stream()
                .collect(toMap(UpdateRateValueRequest::getCurrencyCode, UpdateRateValueRequest::getRateValue));

        // TODO : 벌크성 쿼리 사용으로 변경 필요!
        return Flux
                .fromIterable(requests)
                .flatMap(this::getExchangeRate)
                .flatMap(exchangeRate -> doUpdateRateValue(exchangeRate, currencyCodeMap.get(exchangeRate.getCurrencyCode())))
                .map(UpdateRateValueResponse::of);
    }

    private Mono<ExchangeRate> getExchangeRate(@NonNull final UpdateRateValueRequest request) {
        return loadExchangeRatePort
                .findByCurrencyCode(request.getCurrencyCode())
                .switchIfEmpty(Mono.just(ExchangeRate.withoutId(
                        null,
                        request.getCurrencyCode(),
                        request.getRateValue(),
                        request.getFetchedAt())));
    }

    private Mono<ExchangeRate> doUpdateRateValue(@NonNull final ExchangeRate exchangeRate, final double rateValue) {
        return Mono
                .just(exchangeRate.updateRate(rateValue))
                .flatMap(saveExchangeRatePort::save)
                .map(ExchangeRateHistory::of)
                .flatMap(saveExchangeRateHistoryPort::save)
                .then(Mono.just(exchangeRate));
    }
}
