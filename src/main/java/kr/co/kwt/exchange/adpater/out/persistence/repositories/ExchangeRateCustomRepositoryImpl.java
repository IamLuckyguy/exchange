package kr.co.kwt.exchange.adpater.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ExchangeRateCustomRepositoryImpl implements ExchangeRateCustomRepository {


    @Override
    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
        return null;
    }

    @Override
    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest) {
        return null;
    }

    @Override
    public Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest) {
        return null;
    }
}
