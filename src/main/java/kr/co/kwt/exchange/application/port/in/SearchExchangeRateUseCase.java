package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchExchangeRateUseCase {

    Mono<ExchangeRate> searchExchangeRate(final String currencyCode);

    Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest);

    Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry();

    Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest);

}
