package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.GetExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetExchangeRateService implements GetExchangeRateUseCase {

    private final LoadExchangeRatePort loadExchangeRatePort;

    public Mono<ExchangeRate> getExchangeRateBy(final String currencyCode) {
        return loadExchangeRatePort.findByCurrencyCode(currencyCode);
    }

    @Override
    public Mono<GetExchangeRateResponse> getExchangeRate(final GetExchangeRateRequest request) {
        return loadExchangeRatePort.getExchangeRate(request);
    }

    @Override
    public Flux<GetExchangeRateResponse> getExchangeRates() {
        return loadExchangeRatePort.getExchangeRates();
    }
}
