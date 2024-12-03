package kr.co.kwt.exchange.adpater.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long>, ExchangeRateCustomRepository {

    Mono<ExchangeRate> findByCurrencyCode(String currencyCode);

    Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry();

    Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest);

    Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest);

}