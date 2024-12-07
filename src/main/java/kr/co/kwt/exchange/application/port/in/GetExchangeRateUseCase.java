package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetExchangeRateUseCase {

    Mono<ExchangeRate> getExchangeRateBy(final String currencyCode);

    Mono<GetExchangeRateResponse> getExchangeRate(GetExchangeRateRequest getExchangeRateRequest);

    Flux<GetExchangeRateResponse> getExchangeRates();

}
