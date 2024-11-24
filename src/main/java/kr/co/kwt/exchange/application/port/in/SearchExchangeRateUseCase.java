package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchExchangeRateUseCase {

    Mono<SearchExchangeRateResponse> searchExchangeRate(SearchExchangeRateRequest searchExchangeRateRequest);

    Flux<SearchExchangeRateResponse> searchAllExchangeRate();

}
