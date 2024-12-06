package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SaveExchangeRatePort {

    Mono<ExchangeRate> save(ExchangeRate exchangeRate);

    Flux<ExchangeRate> bulkUpdateRateValues(List<String> exchangeRates, List<Double> rateValues);
}
