package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateCustomRepository {

    Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry();

    Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest);

    Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest);
}
