package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.UpdateRateResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ExchangeRateService implements AddExchangeRateUseCase, UpdateRateUseCase {

    private final SaveExchangeRatePort saveExchangeRatePort;
    private final LoadExchangeRatePort loadExchangeRatePort;

    @Override
    public Mono<AddExchangeRateResponse> addExchangeRate(final AddExchangeRateRequest addExchangeRateRequest) {
        return validateAlreadyExistCountry(addExchangeRateRequest.getCountry())
                .then(createExchangeRate(addExchangeRateRequest))
                .flatMap(saveExchangeRatePort::save)
                .map(ExchangeRate::getId)
                .map(AddExchangeRateResponse::new);
    }

    private Mono<ExchangeRate> validateAlreadyExistCountry(String country) {
        return loadExchangeRatePort
                .findByCountry(country)
                .flatMap(exchangeRate -> {
                    if (exchangeRate != null) {
                        return Mono.error(AlreadyAddedCountryException::new);
                    }

                    return Mono.empty();
                });
    }

    private Mono<ExchangeRate> createExchangeRate(AddExchangeRateRequest addExchangeRateRequest) {
        return Mono.fromSupplier(() -> ExchangeRate.withoutId(addExchangeRateRequest.getCountry(),
                addExchangeRateRequest.getCountryFlag(),
                addExchangeRateRequest.getCountryCode(),
                addExchangeRateRequest.getCurrencyCode(),
                null));
    }

    @Override
    public Mono<UpdateRateResponse> updateRate(final UpdateRateRequest updateRateRequest) {
        return loadExchangeRatePort
                .findByCountry(updateRateRequest.getCountry())
                .map(doUpdateRate(updateRateRequest))
                .flatMap(saveExchangeRatePort::save)
                .map(exchangeRate -> UpdateRateResponse.of(exchangeRate.getRateValue(), exchangeRate.getUpdatedAt()));
    }

    @Override
    public Flux<UpdateRateResponse> updateRates(final Flux<UpdateRateRequest> updateRateRequests) {
        return updateRateRequests.flatMap(this::doUpdateRates);
    }

    private Publisher<? extends UpdateRateResponse> doUpdateRates(UpdateRateRequest updateRateRequest) {
        return loadExchangeRatePort
                .findByCurrencyCode(updateRateRequest.getCurrencyCode().toUpperCase().substring(0, 3))
                .map(doUpdateRate(updateRateRequest))
                .flatMap(saveExchangeRatePort::save)
                .flatMap(exchangeRate -> Mono.just(new UpdateRateResponse(
                        exchangeRate.getRateValue(),
                        exchangeRate.getUpdatedAt())));
    }
    
    private Function<ExchangeRate, ExchangeRate> doUpdateRate(final UpdateRateRequest updateRateRequest) {
        return exchangeRate -> {
            exchangeRate.updateRate(updateRateRequest.getRateValue());
            return exchangeRate;
        };
    }
}
