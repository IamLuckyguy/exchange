package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoadExchangeRatePort {
    
    Flux<ExchangeRate> findAll();

    Mono<ExchangeRate> findByCurrencyCode(String upperCase);

    Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry();

    Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest);

    Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest);

}
