package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.UpdateRateValueUseCase;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueRequest;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateValueResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateRateValueService implements UpdateRateValueUseCase {

    private final LoadExchangeRatePort loadExchangeRatePort;
    private final SaveExchangeRatePort saveExchangeRatePort;

    /**
     * 단일 환율값 업데이트
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Mono<UpdateRateValueResponse> updateRateValue(@NonNull final UpdateRateValueRequest request) {
        return loadExchangeRatePort
                .findByCurrencyCode(request.getCurrencyCode())
                .switchIfEmpty(Mono.error(InvalidCurrencyCodeException::new))
                .flatMap(exchangeRate -> doUpdateRateValue(exchangeRate, request.getRateValue()))
                .map(UpdateRateValueResponse::of);
    }


    private Mono<ExchangeRate> doUpdateRateValue(@NonNull final ExchangeRate exchangeRate, final double rateValue) {
        return Mono
                .just(exchangeRate.updateRate(rateValue))
                .flatMap(saveExchangeRatePort::save);
    }

    /**
     * 다중 환율값 업데이트
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Flux<UpdateRateValueResponse> bulkUpdateRateValues(@NonNull final List<UpdateRateValueRequest> request) {
        List<String> currencyCodes = request
                .stream()
                .map(UpdateRateValueRequest::getCurrencyCode)
                .toList();

        List<Double> rateValues = request
                .stream()
                .map(UpdateRateValueRequest::getRateValue)
                .toList();

        return loadExchangeRatePort
                .findAllByCurrencyCode(currencyCodes)
                .filter(Objects::nonNull)
                .flatMap(exchangeRate -> doBulkUpdateRateValues(currencyCodes, rateValues))
                .map(UpdateRateValueResponse::of);
    }

    private Flux<ExchangeRate> doBulkUpdateRateValues(@NonNull final List<String> currencyCodes,
                                                      @NonNull final List<Double> rateValues
    ) {
        return saveExchangeRatePort.bulkUpdateRateValues(currencyCodes, rateValues);
    }
}
