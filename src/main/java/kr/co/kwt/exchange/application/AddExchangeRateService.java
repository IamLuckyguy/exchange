package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddExchangeRateService implements AddExchangeRateUseCase {

    private final SaveExchangeRatePort saveExchangeRatePort;
    private final LoadExchangeRatePort loadExchangeRatePort;

    /**
     * 환율 도메인 추가
     *
     * @param addExchangeRateRequest
     * @return
     */
    @Override
    public Mono<AddExchangeRateResponse> addExchangeRate(final AddExchangeRateRequest addExchangeRateRequest) {
        return validateAlreadyExistExchangeRate(addExchangeRateRequest.getCurrencyCode())
                .then(doAddExchangeRate(addExchangeRateRequest))
                .map(ExchangeRate::getId)
                .map(AddExchangeRateResponse::new);
    }

    private Mono<ExchangeRate> validateAlreadyExistExchangeRate(final String currencyCode) {
        return loadExchangeRatePort
                .findByCurrencyCode(currencyCode)
                .flatMap(exchangeRate -> {
                    if (exchangeRate != null) {
                        return Mono.error(AlreadyAddedCountryException::new);
                    }

                    return Mono.empty();
                });
    }

    private Mono<ExchangeRate> doAddExchangeRate(@NonNull final AddExchangeRateRequest request) {
        return loadExchangeRatePort
                .findCountryByCurrencyCode(request.getCurrencyCode())
                .switchIfEmpty(Mono.error(InvalidCurrencyCodeException::new))
                .map(country -> ExchangeRate.withoutId(country, request.getCurrencyCode(), request.getRateValue()))
                .flatMap(saveExchangeRatePort::save);
    }
}
