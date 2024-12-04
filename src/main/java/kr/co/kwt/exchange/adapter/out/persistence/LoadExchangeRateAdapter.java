package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateRepository;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoadExchangeRateAdapter implements LoadExchangeRatePort {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Flux<ExchangeRate> findAll() {
        return exchangeRateRepository.findAll();
    }

    @Override
    public Mono<ExchangeRate> findByCurrencyCode(final String currencyCode) {
        return exchangeRateRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
        return exchangeRateRepository.searchAllExchangeRateWithCountry();
    }

    @Override
    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(
            final SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest
    ) {
        return exchangeRateRepository.searchExchangeRateWithCountry(searchExchangeRateWithCountryRequest);
    }

    @Override
    public Mono<SearchCountryResponse> searchCountry(final SearchCountryRequest searchCountryRequest) {
        return exchangeRateRepository.searchCountry(searchCountryRequest);
    }

}
