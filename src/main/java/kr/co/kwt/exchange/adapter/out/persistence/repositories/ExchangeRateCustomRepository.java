package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ExchangeRateCustomRepository {

//    Flux<GetExchangeRateResponse> getExchangeRates();

//    Mono<GetExchangeRateResponse> getExchangeRate(GetExchangeRateRequest getExchangeRateRequest);

    Flux<ExchangeRate> bulkSave(List<ExchangeRate> exchangeRates);
}
