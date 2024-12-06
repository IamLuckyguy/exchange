package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeRateCustomRepository {

    Flux<GetExchangeRateResponse> GetExchangeRates();

    Mono<GetExchangeRateResponse> GetExchangeRate(GetExchangeRateRequest getExchangeRateRequest);

    Flux<ExchangeRate> findAllByCurrencyCode(List<String> currencyCodes);

    Flux<ExchangeRate> bulkUpdateRateValues(List<String> currencyCodes, List<Double> rateValues);

}
